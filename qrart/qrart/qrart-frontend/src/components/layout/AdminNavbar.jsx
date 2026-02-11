import { NavLink, useNavigate } from "react-router-dom";
import { useAuth } from "../../context/AuthContext";
import logo from "../../assets/logo/qrart-logo.png";

export default function AdminNavbar() {
    const { user, logout } = useAuth();
    const navigate = useNavigate();

    const handleLogout = () => {
        logout();
        navigate("/login");
    };

    const linkClass = ({ isActive }) =>
        `
        px-4 py-2 rounded-lg text-sm font-medium transition
        ${isActive
            ? "bg-indigo-600 text-white"
            : "text-slate-300 hover:bg-slate-800 hover:text-white"}
        `;

    return (
        <header className="border-b border-white/10 bg-black/20 backdrop-blur">
            <div className="max-w-7xl mx-auto px-6 h-16 flex items-center justify-between">
                {/* ESQUERDA */}
                <div className="flex items-center gap-6">
                    <div className="flex items-center gap-3">
                        <img src={logo} alt="QrArt" className="w-8 h-8 rounded-lg" />
                        <span className="text-lg font-bold">QrArt Admin</span>
                    </div>

                    <nav className="flex items-center gap-2">
                        <NavLink to="/dashboard" className={linkClass}>
                            Dashboard
                        </NavLink>

                        <NavLink to="/admin/qrcodes" className={linkClass}>
                            QR Codes
                        </NavLink>

                        <NavLink to="/admin/users" className={linkClass}>
                            Usuários
                        </NavLink>
                    </nav>

                </div>

                {/* DIREITA */}
                <div className="flex items-center gap-4">
                    <span className="text-sm text-slate-300">
                        Admin: <strong className="text-white">{user?.name}</strong>
                    </span>
                    <button
                        onClick={handleLogout}
                        className="px-4 py-2 rounded-lg bg-slate-800 hover:bg-slate-700 transition"
                    >
                        Sair
                    </button>
                </div>
            </div>
        </header>
    );
}
