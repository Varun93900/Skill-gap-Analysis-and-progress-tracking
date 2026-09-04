import { useEffect, useState } from "react";

function RoleSelection({ onSelectRole }) {

  const [roles, setRoles] = useState([]);

  useEffect(() => {
    fetchRoles();
  }, []);

  const fetchRoles = async () => {
  const token = localStorage.getItem("token");

  const res = await fetch("https://skill-gap-analysis-and-progress-tracking-production.up.railway.app/api/users/roles", {
  headers: {
    Authorization: `Bearer ${token}`
  }
});

  if (!res.ok) {
    console.error("Failed to fetch roles");
    return;
  }

  const data = await res.json();
  console.log("ROLES:", data); // DEBUG
  setRoles(data);
};

  return (
    <div style={{ marginTop: "40px", textAlign: "center" }}>

      <select onChange={(e) => onSelectRole(e.target.value)}>
        <option value="">-- Select Role --</option>

        {roles.map(role => (
          <option key={role.roleId} value={role.roleId}>
            {role.roleName}
          </option>
        ))}

      </select>
    </div>
  );
}

export default RoleSelection;