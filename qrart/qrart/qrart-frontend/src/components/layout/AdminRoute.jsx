import { Navigate } from "react-router-dom";
import { useAuth } from "../../context/authContext";

export default function AdminRoute({ children }) {
    const { loading, isAuthenticated, isAdmin } = useAuth();

    if (loading) {
        return (
            <div className="min-h-screen flex items-center justify-center bg-[#020617] text-white">
                Carregando...
            </div>
        );
    }

    if (!isAuthenticated) {
        return <Navigate to="/login" replace />;
    }

    if (!isAdmin) {
        return <Navigate to="/dashboard" replace />;
    }

    return children;
}
