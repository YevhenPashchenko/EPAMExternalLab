import React, {useState, useEffect} from 'react';
import {
  Alert,
  Button,
  Form,
  FormFeedback,
  FormGroup,
  Input,
  Label
} from "reactstrap";

const LoginForm = () => {
  const initState = {
    value: "",
    invalidClassName: '',
    isValid: false
  };
  const [email, setEmail] = useState(initState);
  const [password, setPassword] = useState(initState);
  const [showError, setShowError] = useState(false);
  const [pass, setPass] = useState(false);
  const [message, setMessage] = useState("");

  useEffect(() => {
    if (email.isValid && password.isValid) {
      setPass(true);
    } else {
      setPass(false);
    }
  }, [email.isValid, password.isValid]);

  const toggleError = () => {
    setShowError(!showError);
  }

  const validation = (e, setFunction, ...params) => {
    const value = e.currentTarget.value;
    if (value.length === 0 || value.length < params[0] || value.length
        > params[1]) {
      setFunction({
        value: "",
        invalidClassName: "is-invalid",
        isValid: false
      });
    } else {
      setFunction({
        value: value,
        invalidClassName: "is-valid",
        isValid: true
      });
    }
  }

  const login = () => {
    if (!pass) {
      return;
    }
    getClientCode();
  }

  function getClientCode() {
    fetch("http://localhost:9000/login/base-client", {
      method: "GET"
    }).then(res => {
      if (res.status === 200) {
        res.json().then(result => {
          getToken(result.code, "base-client", "base-client-secret",
              getPersonCode);
        });
      } else {
        getError(res);
      }
    });
  }

  function getError(response) {
    response.json().then(error => {
      setMessage(error.message);
      toggleError();
    });
  }

  function getToken(code, clientId, clientSecret, func) {
    fetch("http://localhost:9000/oauth2/token", {
      method: "POST",
      headers: {
        Authorization: "Basic " + btoa(`${clientId}:${clientSecret}`),
        "Content-Type": "application/x-www-form-urlencoded"
      },
      body: new URLSearchParams({
        grant_type: "authorization_code",
        code: code,
        redirect_uri: "http://host.docker.internal:9000/login"
      })
    }).then(res => {
      if (res.status === 200) {
        res.json().then(result => {
          func(result);
        });
      } else {
        getError(res);
      }
    });
  }

  function getPersonCode(token) {
    fetch("http://localhost:9000/login", {
      method: "POST",
      headers: {
        Authorization: "Bearer " + token.access_token,
        "Content-Type": "application/json"
      },
      body: JSON.stringify({
        "client-id": "gift-certificates-client",
        "email": email.value,
        "password": password.value,
        "redirect-uri": "http://host.docker.internal:9000/login",
        "scopes": [
          "gift-certificates.read",
          "gift-certificates.write"
        ]
      })
    }).then(res => {
      if (res.status === 200) {
        res.json().then(result => {
          getToken(result.code, "gift-certificates-client",
              "gift-certificates-client-secret", storePersonData);
        });
      } else {
        getError(res);
      }
    });
  }

  function storePersonData(token) {
    sessionStorage.setItem('user-email', JSON.stringify(email.value));
    sessionStorage.setItem('user-token', JSON.stringify(token));
    window.location.reload();
  }

  return (
      <div
          className="d-flex min-vh-100 bg-secondary bg-opacity-50 justify-content-center align-items-center">
        <Form className="w-25 bg-light py-3 px-5">
          <h5 className="pb-3 text-center">LogIn</h5>
          <FormGroup>
            <Label className="w-100">
              <Input onBlur={(e) => validation(e, setEmail, 3, 30)}
                     className={email.invalidClassName} type="email"
                     name="email" placeholder="Username"
                     aria-invalid={email.isValid}/>
              <FormFeedback>Email field cannot be empty</FormFeedback>
            </Label>
          </FormGroup>
          <FormGroup>
            <Label className="w-100">
              <Input onBlur={(e) => {
                validation(e, setPassword, 4, 30)
              }} className={password.invalidClassName} type="password"
                     name="password" placeholder="Password"
                     aria-invalid={password.isValid}/>
              <FormFeedback>Password length must be more than 4 and less than 30
                characters</FormFeedback>
            </Label>
          </FormGroup>
          <Alert color="danger" isOpen={showError}
                 toggle={toggleError}>{message}</Alert>
          <div className="d-flex justify-content-center">
            <Button onClick={login} color="primary" size="sm">Login</Button>
          </div>
        </Form>
      </div>
  );
};

export default LoginForm;