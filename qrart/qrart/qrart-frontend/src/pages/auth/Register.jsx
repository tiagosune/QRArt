import { useEffect, useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import { authService } from "../../services/authService";
import AuthLayout from "../../components/layout/AuthLayout";
import Card from "../../components/ui/Card";
import Input from "../../components/ui/Input";
import Button from "../../components/ui/Button";

export default function Register() {
    const [name, setName] = useState("");
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");
    const [error, setError] = useState("");
    const [loading, setLoading] = useState(false);
    const [dark, setDark] = useState(true);

    const navigate = useNavigate();

    // Dark como padrão
    useEffect(() => {
        const theme = localStorage.getItem("theme") || "dark";
        document.documentElement.classList.toggle("dark", theme === "dark");
        setDark(theme === "dark");
    }, []);

    const toggleTheme = () => {
        const html = document.documentElement;
        const isDark = html.classList.contains("dark");

        html.classList.toggle("dark", !isDark);
        localStorage.setItem("theme", isDark ? "light" : "dark");
        setDark(!isDark);
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        setError("");
        setLoading(true);

        try {
            await authService.register(name, email, password);
            navigate("/login");
        } catch (err) {
            setError(err.response?.data?.message || "Erro ao criar conta");
        } finally {
            setLoading(false);
        }
    };

    return (
        <AuthLayout>
            {/* Toggle tema */}
            <button
                onClick={toggleTheme}
                className="absolute top-6 right-6 w-10 h-10 rounded-full
                bg-slate-800 text-slate-200 hover:bg-slate-700
                dark:bg-slate-700 dark:text-yellow-300 transition"
            >
                {dark ? "🌙" : "☀️"}
            </button>

            <Card>
                {/* Header compacto */}
                <div className="mb-6 text-center">
                    <h1 className="text-3xl font-bold text-slate-900 dark:text-white">
                        Criar conta
                    </h1>

                    <p className="text-sm mt-1 text-slate-600 dark:text-slate-400">
                        Comece a criar seus QR Codes agora
                    </p>
                </div>

                {/* Error */}
                <div className="min-h-[20px] mb-3 text-center">
                    {error && (
                        <p className="text-sm text-red-500">
                            {error}
                        </p>
                    )}
                </div>

                {/* Form */}
                <form onSubmit={handleSubmit} className="flex flex-col gap-4">
                    <Input
                        label="Nome"
                        placeholder="Seu nome"
                        value={name}
                        onChange={(e) => setName(e.target.value)}
                        required
                    />

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
                        placeholder="Crie uma senha"
                        value={password}
                        onChange={(e) => setPassword(e.target.value)}
                        required
                        minLength={6}
                    />

                    <div className="pt-2">
                        <Button loading={loading}>
                            Criar conta
                        </Button>
                    </div>
                </form>

                {/* Footer */}
                <p className="mt-6 text-sm text-center text-slate-600 dark:text-slate-400">
                    Já tem conta?{" "}
                    <Link
                        to="/login"
                        className="text-indigo-400 hover:text-indigo-300 font-semibold"
                    >
                        Entrar
                    </Link>
                </p>
            </Card>
        </AuthLayout>
    );
}
