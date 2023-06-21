import React, {createContext, useEffect, useState} from "react";
import Header from "./components/Header";
import Footer from "./components/Footer";
import LoginForm from "./components/LoginForm";
import {BrowserRouter, Navigate, Route, Routes} from "react-router-dom";
import CertificateList from "./components/CertificateList";

export const AuthContext = createContext(null);

function App() {
  const [email, setEmail] = useState(null);
  const [token, setToken] = useState(null);

  useEffect(() => {
    const email = sessionStorage.getItem("user-email");
    const token = sessionStorage.getItem("user-token");
    if (email && token) {
      setEmail(JSON.parse(email))
      setToken(JSON.parse(token));
    }
  }, [])

  return (
      <AuthContext.Provider value={{email, setEmail, token, setToken}}>
        <BrowserRouter>
          <div className="App min-vh-100">
            <Header/>
            {
              token
                  ?
                  <Routes>
                    <Route path="/certificates" element={<CertificateList/>}/>
                    <Route path="*"
                           element={<Navigate replace to="/certificates"/>}/>
                  </Routes>
                  :
                  <Routes>
                    <Route path="/login" element={<LoginForm/>}/>
                    <Route path="*" element={<Navigate replace to="/login"/>}/>
                  </Routes>
            }
            <Footer/>
          </div>
        </BrowserRouter>
      </AuthContext.Provider>
  );
}

export default App;
