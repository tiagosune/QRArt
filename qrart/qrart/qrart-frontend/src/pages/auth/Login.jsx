import { useEffect, useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import { authService } from "../../services/authService";
import { useAuth } from "../../context/AuthContext";
import AuthLayout from "../../components/layout/AuthLayout";
import Card from "../../components/ui/Card";
import Input from "../../components/ui/Input";
import Button from "../../components/ui/Button";
import logo from "../../assets/logo/qrart-logo.png";

export default function Login() {
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");
    const [error, setError] = useState("");
    const [loading, setLoading] = useState(false);
    const [dark, setDark] = useState(true);

    const navigate = useNavigate();
    const { login } = useAuth();

    useEffect(() => {
        document.documentElement.classList.add("dark");
        localStorage.setItem("theme", "dark");
    }, []);

    const toggleTheme = () => {
        const html = document.documentElement;
        const isDark = html.classList.contains("dark");

        html.classList.toggle("dark");
        setDark(!isDark);
        localStorage.setItem("theme", isDark ? "light" : "dark");
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        if (loading) return;

        setError("");
        setLoading(true);

        try {
            const data = await authService.login(email, password);
            await login(data);
            navigate("/dashboard", { replace: true });
        } catch (err) {
            setError(err.response?.data?.message || "Erro ao fazer login");
        } finally {
            setLoading(false);
        }
    };

    return (
        <AuthLayout>
            {/* Toggle */}
            <button
                onClick={toggleTheme}
                className="absolute top-6 right-6 w-10 h-10 rounded-full
                bg-slate-800 text-yellow-300 hover:bg-slate-700 transition"
            >
                {dark ? "🌙" : "☀️"}
            </button>

            <Card className="py-10">
                {/* Logo */}
                <div className="flex flex-col items-center mb-8">
                    <img
                        src={logo}
                        alt="QrArt"
                        className="w-20 h-20 mb-4 object-contain"
                    />

                    <h1 className="text-3xl font-bold text-white">
                        Bem-vindo de volta
                    </h1>

                    <p className="text-sm text-slate-400 mt-1">
                        Acesse sua plataforma
                    </p>
                </div>

                {/* Error */}
                <div className="min-h-[24px] text-center mb-4">
                    {error && (
                        <p className="text-sm text-red-500">{error}</p>
                    )}
                </div>

                {/* Form */}
                <form onSubmit={handleSubmit} className="flex flex-col gap-5">
                    <Input
                        label="Email"
                        placeholder="Digite seu email"
                        value={email}
                        onChange={(e) => setEmail(e.target.value)}
                        required
                    />

                    <Input
                        label="Senha"
                        type="password"
                        placeholder="Digite sua senha"
                        value={password}
                        onChange={(e) => setPassword(e.target.value)}
                        required
                    />

                    <Button type="submit" loading={loading}>
                        Entrar
                    </Button>
                </form>

                {/* Footer */}
                <p className="mt-6 text-sm text-center text-slate-400">
                    Não tem conta?{" "}
                    <Link
                        to="/register"
                        className="text-indigo-400 hover:text-indigo-300 font-semibold"
                    >
                        Criar agora
                    </Link>
                </p>
            </Card>
        </AuthLayout>
    );
}
