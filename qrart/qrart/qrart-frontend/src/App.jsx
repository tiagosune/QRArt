import { BrowserRouter, Routes, Route, Navigate } from "react-router-dom";
import { AuthProvider, useAuth } from "./context/AuthContext";

import Login from "./pages/auth/Login";
import Register from "./pages/auth/Register";
import Dashboard from "./pages/Dashboard";
import QRCodeDetails from "./pages/QRCodeDetails";
import CreateQRCode from "./pages/CreateQRCode";
import Success from "./pages/Success";
import Cancel from "./pages/Cancel";
import AdminRoute from "./components/layout/AdminRoute";
import AdminQRCodes from "./pages/admin/AdminQRCodes";
import AdminUsers from "./pages/admin/AdminUsers.jsx";


const PrivateRoute = ({ children }) => {
    const { user, loading } = useAuth();

    if (loading) {
        return (
            <div className="min-h-screen flex items-center justify-center bg-[#020617] text-white">
                Carregando...
            </div>
        );
    }

    if (!user) {
        return <Navigate to="/login" replace />;
    }

    return children;
};

export default function App() {
    return (
        <AuthProvider>
            <BrowserRouter>
                <Routes>
                    {/* Públicas */}
                    <Route path="/login" element={<Login />} />
                    <Route path="/register" element={<Register />} />

                    {/* Privadas */}
                    <Route
                        path="/dashboard"
                        element={
                            <PrivateRoute>
                                <Dashboard />
                            </PrivateRoute>
                        }
                    />

                    <Route
                        path="/create"
                        element={
                            <PrivateRoute>
                                <CreateQRCode />
                            </PrivateRoute>
                        }
                    />

                    <Route
                        path="/qrcode/:id"
                        element={
                            <PrivateRoute>
                                <QRCodeDetails />
                            </PrivateRoute>
                        }
                    />

                    {/* Stripe */}
                    <Route
                        path="/success"
                        element={
                            <PrivateRoute>
                                <Success />
                            </PrivateRoute>
                        }
                    />
                    <Route
                        path="/cancel"
                        element={
                            <PrivateRoute>
                                <Cancel />
                            </PrivateRoute>
                        }
                    />
                    <Route
                        path="/admin/qrcodes"
                        element={
                            <AdminRoute>
                                <AdminQRCodes />
                            </AdminRoute>
                        }
                    />
                    <Route
                        path="/admin/users"
                        element={
                            <AdminRoute>
                                <AdminUsers />
                            </AdminRoute>
                        }
                    />

                    {/* Default */}
                    <Route path="/" element={<Navigate to="/dashboard" replace />} />
                </Routes>
            </BrowserRouter>
        </AuthProvider>
    );
}
