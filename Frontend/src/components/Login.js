import { useState } from "react";
import { useNavigate } from "react-router-dom"; // ✅ ADD THIS
import "./Login.css";

function Login() {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");

  const navigate = useNavigate(); // ✅ ADD THIS

  const handleLogin = async () => {
    try {
      const response = await fetch("https://skill-gap-analysis-and-progress-tracking-production.up.railway.app/api/users/login", {
        method: "POST",
        headers: {
          "Content-Type": "application/json"
        },
        body: JSON.stringify({
          email,
          password
        })
      });

      if (!response.ok) {
        const errorText = await response.text();
        alert(errorText);
        return;
      }

      const token = await response.text();
      localStorage.setItem("token", token);

      window.location.href = "/dashboard";

    } catch (error) {
      alert("Error connecting to server");
    }
  };

  return (
    <div className="login-container">
      <div className="login-card">

        <h2>Login</h2>

        <input
          type="email"
          name="email"
          placeholder="Email ID"
          autoComplete="email"
          defaultValue=""
          onChange={(e) => setEmail(e.target.value)}
        />

        <input
          type="password"
          name="password"
          placeholder="Password"
          autoComplete="current-password"
          defaultValue=""
          onChange={(e) => setPassword(e.target.value)}
        />

        <button onClick={handleLogin}>LOGIN</button>

        <p style={{ marginTop: "10px" }}>
          Don’t have an account?{" "}
          <span
            style={{ color: "blue", cursor: "pointer" }}
            onClick={() => navigate("/register")} 
          >
            Register
          </span>
        </p>

      </div>
    </div>
  );
}

export default Login;