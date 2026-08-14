import { useEffect, useState } from "react";
import "./Dashboard.css";
import RoleSelection from "./RoleSelection";

function Dashboard() {
  const [progress, setProgress] = useState(null);
  const [skills, setSkills] = useState([]);
  const [selectedRole, setSelectedRole] = useState(null);
  const [, setSkillGap] = useState(null);
  const [roadmap, setRoadmap] = useState([]);
  const [selectedSkillTopics, setSelectedSkillTopics] = useState([]);
  const [selectedSkillName, setSelectedSkillName] = useState("");
  const [manualSkill, setManualSkill] = useState("");

  // ============================
  // FETCH FUNCTIONS
  // ============================

  const fetchProgress = async () => {
    try {
      const token = localStorage.getItem("token");

      const res = await fetch("http://localhost:8080/api/users/progress", {
        headers: { Authorization: `Bearer ${token}` }
      });

      if (!res.ok) throw new Error("Progress fetch failed");

      const data = await res.json();
      setProgress(data);
    } catch (err) {
      console.error(err);
    }
  };

  const fetchSkills = async () => {
    try {
      const token = localStorage.getItem("token");

      const res = await fetch("http://localhost:8080/api/users/skills", {
        headers: { Authorization: `Bearer ${token}` }
      });

      if (!res.ok) {
        const text = await res.text();
        console.error("ERROR:", text);
        return;
      }

      const data = await res.json();
      setSkills(Array.isArray(data) ? data : []);
    } catch (err) {
      console.error(err);
    }
  };

  const fetchSkillGap = async (roleId) => {
    try {
      const token = localStorage.getItem("token");

      const res = await fetch(
        `http://localhost:8080/api/users/skill-gap?roleId=${roleId}`,
        {
          headers: { Authorization: `Bearer ${token}` }
        }
      );

      if (!res.ok) throw new Error("Skill gap error");

      const data = await res.json();
      setSkillGap(data || null);
    } catch (err) {
      console.error(err);
      setSkillGap(null);
    }
  };

  const fetchRoadmap = async (roleId) => {
    try {
      const token = localStorage.getItem("token");

      const res = await fetch(
        `http://localhost:8080/api/users/roadmap?roleId=${roleId}`,
        {
          headers: { Authorization: `Bearer ${token}` }
        }
      );

      if (!res.ok) throw new Error("Roadmap error");

      const data = await res.json();
      setRoadmap(Array.isArray(data) ? data : []);
    } catch (err) {
      console.error(err);
      setRoadmap([]);
    }
  };

 const handleAddSkill = async () => {
  if (!manualSkill.trim()) return;

  const exists = skills.some(
    s => s.skill.skillName.toLowerCase() === manualSkill.toLowerCase()
  );

  if (exists) {
    alert("Skill already added");
    return;
  }

  try {
    const token = localStorage.getItem("token");

    await fetch("http://localhost:8080/api/users/add-skill", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
        Authorization: `Bearer ${token}`
      },
      body: JSON.stringify({ skillName: manualSkill })
    });

    // 🔥 ADD THIS LINE (important fix)
    await fetchProgress();

    // existing
    await fetchSkills();

    if (selectedRole) {
      await fetchSkillGap(selectedRole);
      await fetchRoadmap(selectedRole);
    }

    setManualSkill("");

  } catch (err) {
    console.error(err);
  }
};
  // ============================
  // ROLE SELECT
  // ============================

  const handleRoleSelect = (roleId) => {
    if (!roleId) {
      setSelectedRole(null);
      setSkillGap(null);
      setRoadmap([]);
      return;
    }

    const id = Number(roleId);

    setSelectedRole(id);
    fetchSkillGap(id);
    fetchRoadmap(id);
  };

  // ============================
  // INITIAL LOAD
  // ============================

  useEffect(() => {
    fetchProgress();
    fetchSkills();
  }, []);

  // ============================
  // ✅ FIXED CALCULATIONS
  // ============================
// ✅ TOTAL = roadmap (always correct)
const totalRequired = roadmap.length;

// ✅ COMPLETED = based on skills
const completedRequired = roadmap.filter(item =>
  skills.some(
    s =>
      s.skill.skillName.toLowerCase() === item.skillName.toLowerCase() &&
      s.completed === true
  )
).length;

// ✅ REMAINING
const remainingCount = totalRequired - completedRequired;

// ✅ PROGRESS
const roleProgress =
  totalRequired === 0
    ? 0
    : Math.round((completedRequired / totalRequired) * 100);
const missingSkills = roadmap
  .filter(item => {
    const userSkill = skills.find(
      s => s.skill.skillId === item.skillId
    );

    return !userSkill || userSkill.completed === false;
  })
  .map(item => item.skillName);

// ✅ COMPLETED (sidebar)
const completedCount = selectedRole
  ? roadmap.filter(item =>
      skills.some(
        s =>
          s.skill.skillId === item.skillId &&
          s.completed === true
      )
    ).length
  : 0;
  

// ✅ MISSING (derive instead of backend)
const missingCount = missingSkills.length;
  // ============================
  // MARK COMPLETE
  // ============================

  const markSkillComplete = async (skillName) => {
    try {
      const token = localStorage.getItem("token");

      let found = skills.find(
        s =>
          s.skill.skillName.toLowerCase() ===
          skillName.toLowerCase()
      );

      // ADD skill if not exists
      if (!found) {
        const res = await fetch(
          `http://localhost:8080/api/users/add-skill`,
          {
            method: "POST",
            headers: {
              "Content-Type": "application/json",
              Authorization: `Bearer ${token}`
            },
            body: JSON.stringify({ skillName })
          }
        );

        if (!res.ok) {
          alert("Failed to add skill");
          return;
        }

        await fetchSkills();

        const updatedRes = await fetch(
          "http://localhost:8080/api/users/skills",
          {
            headers: { Authorization: `Bearer ${token}` }
          }
        );

        const updatedSkills = await updatedRes.json();

        found = updatedSkills.find(
          s =>
            s.skill.skillName.toLowerCase() ===
            skillName.toLowerCase()
        );
      }

      // MARK COMPLETE
      await fetch(
        `http://localhost:8080/api/users/skills/${found.skill.skillId}/complete`,
        {
          method: "PUT",
          headers: { Authorization: `Bearer ${token}` }
        }
      );

      // REFRESH DATA
      await fetchProgress();
      await fetchSkills();

      if (selectedRole) {
        await fetchSkillGap(selectedRole);
      }

    } catch (err) {
      console.error(err);
    }
  };

const markSkillIncomplete = async (skillId) => {
  try {
    const token = localStorage.getItem("token");

    await fetch(
      `http://localhost:8080/api/users/skills/${skillId}/incomplete`,
      {
        method: "PUT",
        headers: {
          Authorization: `Bearer ${token}`
        }
      }
    );

    // 🔥 ADDED THIS LINE
    await fetchProgress();

    // existing
    await fetchSkills();
    await fetchSkillGap(selectedRole);
    await fetchRoadmap(selectedRole);

  } catch (err) {
    console.error(err);
  }
};

  // ============================
  // SORT ROADMAP
  // ============================

  const filteredRoadmap = [...roadmap].sort((a, b) => {
    const order = { HIGH: 1, MEDIUM: 2, LOW: 3 };
    return order[a.priority] - order[b.priority];
  });

  // ============================
  // UI
  // ============================

  return (
    <div className="dashboard-container">
      <div className="layout">

        <div className="sidebar">
          <h1>SkillGap Dashboard</h1>

          <div className="card">
            Total Skills<br />{progress?.totalSkills ?? 0}
          </div>

          <div className="card">
            Completed<br />{completedCount}
          </div>

          <div className="card">
            Remaining<br />{remainingCount}
          </div>
        </div>

        <div className="main">

          <div className="top-section">
            
            <div className="top-inner-row">

  {/* Role */}
  <div className="card wide">
    <h3>Select Your Targeted Role :</h3>
    <RoleSelection onSelectRole={handleRoleSelect} />
  </div>

  {/* Add Skill */}
  <div className="card wide">
    <h3>Add Your Skill :</h3>

    <input
      type="text"
      placeholder="Enter skill (e.g. Java, React)"
      value={manualSkill}
      onChange={(e) => setManualSkill(e.target.value)}
    />

    <button onClick={handleAddSkill}>
      Add Skill
    </button>
  </div>

</div>

            <div className="card wide">
              <h3>Progress Bar : {roleProgress}%</h3>

              <div className="progress-bar">
                <div
                  className="progress-fill"
                  style={{ width: `${roleProgress}%` }}
                />
              </div>
            </div>
          </div>

          <div className="card stats">
            <h3>Stats :</h3>

            <div className="stats-row">
              <div className="mini-card">
                Total Required<br />{totalRequired}
              </div>

              <div className="mini-card">
                Missing Skills<br />{missingCount}
              </div>

              <div className="mini-card">
                Missing List<br />
                {missingSkills.length > 0 ? (
  missingSkills.map((s, i) => (
    <p key={i}>{s}</p>
  ))
) : (
  <p>No missing skills 🎉</p>
)}
              </div>
            </div>
          </div>

          <div className="bottom-section">

            <div className="card">
              <h3>Selected Skill :</h3>

              {selectedSkillName && (
  <>
    <p><b>{selectedSkillName}</b></p>

    <div className="learn-section">
      <p><b>What to Learn :</b></p>

      {selectedSkillTopics.map((t, i) => (
        <p key={i}>• {t}</p>
      ))}
    </div>

    {(() => {
      const found = skills.find(
        s =>
          s.skill.skillName.toLowerCase() ===
          selectedSkillName.toLowerCase()
      );

      const isCompleted = found?.completed === true;

      return isCompleted ? (
        <button
          onClick={() => markSkillIncomplete(found.skill.skillId)}
        >
          Undo ❌
        </button>
      ) : (
        <button onClick={() => markSkillComplete(selectedSkillName)}>
          Mark Complete ✅
        </button>
      );
    })()}
  </>
)}
            </div>
            <div className="card roadmap">
              <h3>RoadMap :</h3>

              <div className="roadmap-grid">
                {filteredRoadmap.map((item, i) => {
                  const isCompleted = skills.some(
                    s =>
                      s.skill.skillName.toLowerCase() === item.skillName.toLowerCase() &&
                      s.completed === true
                  );

                  return (
                    <div
                      key={i}
                      className={`mini-card ${isCompleted ? "completed-skill" : ""}`}
                      onClick={async () => {
                        try {
                          setSelectedSkillName(item.skillName);

                          const res = await fetch(
                            `http://localhost:8080/api/users/learning-content?skillId=${item.skillId}`
                          );

                          const data = await res.json();
                          setSelectedSkillTopics(data);
                        } catch (err) {
                          console.error(err);
                        }
                      }}
                    >
                      {item.skillName}
                      <br />
                      {item.priority}
                      <br />
                      {isCompleted && <span>✅ Completed</span>}
                    </div>
                  );
                })}
              </div>
            </div>

          </div>

        </div>
      </div>
    </div>
  );
}

export default Dashboard;