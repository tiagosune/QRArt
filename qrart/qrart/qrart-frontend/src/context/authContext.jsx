import { createContext, useContext, useEffect, useState } from "react";
import { authService } from "../services/authService";

const AuthContext = createContext();

export const AuthProvider = ({ children }) => {
    const [user, setUser] = useState(null);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        const token = localStorage.getItem("token");
        const userName = localStorage.getItem("userName");

        if (token && userName) {
            setUser({ name: userName });
        } else {
            setUser(null);
        }

        setLoading(false);
    }, []);

    const login = (userData) => {
        localStorage.setItem("token", userData.token);
        localStorage.setItem("userName", userData.name);
        setUser({ name: userData.name });
    };

    const logout = () => {
        authService.logout();
        localStorage.removeItem("userName");
        setUser(null);
    };

    return (
        <AuthContext.Provider value={{ user, login, logout, loading }}>
            {children}
        </AuthContext.Provider>
    );
};

export const useAuth = () => useContext(AuthContext);
