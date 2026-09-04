import { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";

function Register() {
  const [name, setName] = useState("");
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [confirmPassword, setConfirmPassword] = useState("");
  const [otp, setOtp] = useState("");
  const [showOtpInput, setShowOtpInput] = useState(false);

  // 🔥 NEW STATES
  const [timer, setTimer] = useState(60);
  const [canResend, setCanResend] = useState(false);

  const navigate = useNavigate();

  // 🔥 TIMER LOGIC
  useEffect(() => {
    let interval;

    if (showOtpInput && timer > 0) {
      interval = setInterval(() => {
        setTimer(prev => prev - 1);
      }, 1000);
    }

    if (timer === 0) {
      setCanResend(true);
      clearInterval(interval);
    }

    return () => clearInterval(interval);
  }, [showOtpInput, timer]);

  // 🔥 REGISTER → SEND OTP
  const handleRegister = async () => {
    if (!name || !email || !password || !confirmPassword) {
      alert("Please fill all fields");
      return;
    }

    if (password !== confirmPassword) {
      alert("Passwords do not match");
      return;
    }

    try {
      const res = await fetch("https://skill-gap-analysis-and-progress-tracking-production.up.railway.app/api/users/register", {
        method: "POST",
        headers: {
          "Content-Type": "application/json"
        },
        body: JSON.stringify({
          name,
          email,
          password,
          confirmPassword
        })
      });

      const data = await res.text();

      if (!res.ok) {
        alert(data);
        return;
      }

      alert("OTP sent to your email 📩");

      setShowOtpInput(true);

      // 🔥 START TIMER
      setTimer(60);
      setCanResend(false);

    } catch (err) {
      console.error(err);
      alert("Server error");
    }
  };

  // 🔥 VERIFY OTP
  const handleVerifyOtp = async () => {
    if (!otp) {
      alert("Enter OTP");
      return;
    }

    try {
      const res = await fetch(
        `https://skill-gap-analysis-and-progress-tracking-production.up.railway.app/api/users/verify-otp?email=${email}&otp=${otp}`,
        {
          method: "POST"
        }
      );

      const data = await res.text();

      if (!res.ok) {
        alert(data);
        return;
      }

      alert("Email verified successfully ✅");

      navigate("/");

    } catch (err) {
      console.error(err);
      alert("Verification failed");
    }
  };

  // 🔥 RESEND OTP
  const handleResendOtp = async () => {
    try {
      const res = await fetch(
        `https://skill-gap-analysis-and-progress-tracking-production.up.railway.app/api/users/resend-otp?email=${email}`,
        {
          method: "POST"
        }
      );

      const data = await res.text();

      if (!res.ok) {
        alert(data);
        return;
      }

      alert("OTP resent 📩");

      // 🔥 RESET TIMER
      setTimer(60);
      setCanResend(false);

    } catch (err) {
      console.error(err);
      alert("Failed to resend OTP");
    }
  };

  return (
    <div className="login-container">
      <div className="login-card">

        <h2>Register</h2>

        {!showOtpInput && (
          <>
            <input
              type="text"
              placeholder="Name"
              value={name}
              onChange={(e) => setName(e.target.value)}
            />

            <input
              type="email"
              placeholder="Email"
              value={email}
              onChange={(e) => setEmail(e.target.value)}
            />

            <input
              type="password"
              placeholder="Password"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
            />

            <input
              type="password"
              placeholder="Confirm Password"
              value={confirmPassword}
              onChange={(e) => setConfirmPassword(e.target.value)}
            />

            <button onClick={handleRegister}>
              Register
            </button>
          </>
        )}

        {showOtpInput && (
          <>
            <input
              type="text"
              placeholder="Enter OTP"
              value={otp}
              onChange={(e) => setOtp(e.target.value)}
            />

            <button onClick={handleVerifyOtp}>
              Verify OTP
            </button>

            {/* 🔥 RESEND UI */}
            {!canResend ? (
              <p style={{ marginTop: "10px" }}>
                Resend OTP in {timer}s
              </p>
            ) : (
              <button onClick={handleResendOtp}>
                Resend OTP
              </button>
            )}
          </>
        )}

        <p style={{ marginTop: "10px" }}>
          Already have an account?{" "}
          <span
            style={{ color: "blue", cursor: "pointer" }}
            onClick={() => navigate("/")}
          >
            Login
          </span>
        </p>

      </div>
    </div>
  );
}

export default Register;