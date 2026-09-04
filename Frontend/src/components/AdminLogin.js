import { useState } from "react";
import "./Login.css";
import { useNavigate } from "react-router-dom";

function AdminLogin() {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [showPassword, setShowPassword] = useState(false); // ✅ NEW
  const navigate = useNavigate();

  const handleLogin = async () => {
    if (!email || !password) {
      alert("Please enter email & password");
      return;
    }

    try {
      const res = await fetch("https://skill-gap-analysis-and-progress-tracking-production.up.railway.app/api/admin/login", {
        method: "POST",
        headers: {
          "Content-Type": "application/json"
        },
        body: JSON.stringify({ email, password })
      });

      const data = await res.text();

      if (!res.ok) {
        alert(data);
        return;
      }

     localStorage.setItem("adminToken", data);
alert("Admin Login Successful ✅");

// ✅ ADD THIS LINE ONLY
navigate("/admin/dashboard")

    } catch (err) {
      console.error(err);
      alert("Login failed");
    }
  };

  return (
    <div className="login-container">
      <div className="login-card">


        <h2>Admin Login</h2>

       <div style={{ width: "100%" }}>
  <input
    type="email"
    placeholder="Email"
    onChange={(e) => setEmail(e.target.value)}
    style={{ width: "100%", boxSizing: "border-box" }}
  />
</div>

        {/* 🔥 PASSWORD WITH EYE */}
        <div style={{ position: "relative",width: "100%" }}>
          <input
            type={showPassword ? "text" : "password"} // ✅ toggle
            placeholder="Password"
            onChange={(e) => setPassword(e.target.value)}
            style={{ width: "100%", paddingRight: "40px", boxSizing: "border-box" }}
          />

          <span
            onClick={() => setShowPassword(!showPassword)}
            style={{
              position: "absolute",
              right: "10px",
              top: "50%",
              transform: "translateY(-50%)",
              cursor: "pointer",
              color: "white"
            }}
          >
           
          </span>
        </div>

        <button onClick={handleLogin}>
          Login
        </button>

      </div>
    </div>
  );
}

export default AdminLogin;