import {useEffect, useState} from "react";
import {Link, useNavigate} from "react-router-dom";
import {qrcodeService} from "../services/qrcodeService";
import {useAuth} from "../context/AuthContext";
import logo from "../assets/logo/qrart-logo.png";

export default function Dashboard() {
    const [qrcodes, setQrcodes] = useState([]);
    const [loading, setLoading] = useState(true);
    const navigate = useNavigate();
    const {user, logout} = useAuth();

    useEffect(() => {
        loadQRCodes();
    }, []);

    const loadQRCodes = async () => {
        try {
            const data = await qrcodeService.getAll();
            setQrcodes(data);
        } catch (err) {
            console.error("Erro ao carregar QR Codes", err);
        } finally {
            setLoading(false);
        }
    };

    const handleLogout = () => {
        logout();
        navigate("/login");
    };

    if (loading) {
        return (
            <div className="min-h-screen flex items-center justify-center bg-[#020617] text-slate-300">
                Carregando...
            </div>
        );
    }

    return (
        <div className="min-h-screen bg-[#020617] text-white">
            {/* NAVBAR */}
            <header className="border-b border-white/10 bg-black/20 backdrop-blur">
                <div className="max-w-7xl mx-auto px-6 h-16 flex items-center justify-between">
                    <div className="flex items-center gap-3">
                        <img src={logo} alt="QrArt" className="w-8 h-8 rounded-lg"/>
                        <span className="text-lg font-bold">QrArt</span>
                    </div>

                    <div className="flex items-center gap-4">
            <span className="text-sm text-slate-300">
              Olá, <strong className="text-white">{user?.name}</strong>
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

            {/* CONTEÚDO */}
            <main className="max-w-7xl mx-auto px-6 py-10">
                {/* HEADER */}
                <div className="flex flex-col sm:flex-row sm:items-center sm:justify-between gap-6 mb-10">
                    <div>
                        <h1 className="text-3xl font-bold mb-1">Meus QR Codes</h1>
                        <p className="text-slate-400">
                            Gerencie seus QR Codes personalizados
                        </p>
                    </div>

                    <Link
                        to="/create"
                        className="inline-flex items-center justify-center px-6 py-3 bg-indigo-600 hover:bg-indigo-500 rounded-xl font-semibold transition"
                    >
                        + Criar QR Code
                    </Link>
                </div>

                {/* GRID */}
                {qrcodes.length > 0 ? (
                    <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 gap-6">
                        {qrcodes.map((qr) => (
                            <div
                                key={qr.id}
                                onClick={() => navigate(`/qrcode/${qr.id}`)}
                                className="
                  bg-slate-900/60
                  border border-white/10
                  rounded-2xl
                  p-4
                  hover:border-indigo-500/40
                  hover:-translate-y-1
                  transition-all
                  cursor-pointer
                  group
                "
                            >
                                {/* PREVIEW QR */}
                                <div className="h-36 flex items-center justify-center bg-black/30 rounded-xl mb-4">
                                    {qr.paid && qr.imgPath ? (
                                        <img
                                            src={`http://localhost:8080${qr.imgPath}`}
                                            alt={qr.title}
                                            className="max-h-28 max-w-28 object-contain transition-transform group-hover:scale-105"
                                        />
                                    ) : (
                                        <span className="text-sm text-slate-400">
                      Pagamento pendente
                    </span>
                                    )}
                                </div>

                                {/* STATUS */}
                                <span
                                    className={`inline-block mb-2 px-3 py-1 rounded-full text-xs font-semibold ${
                                        qr.paid
                                            ? "bg-emerald-500/20 text-emerald-400"
                                            : "bg-amber-500/20 text-amber-400"
                                    }`}
                                >
                  {qr.paid ? "Pago" : "Pendente"}
                </span>

                                {/* TÍTULO */}
                                <h3 className="text-sm font-semibold truncate">
                                    {qr.title || `QR Code #${qr.id}`}
                                </h3>

                                {/* TEXTO */}
                                <p className="text-xs text-slate-400 line-clamp-2 mt-1 mb-4">
                                    {qr.text}
                                </p>

                                {/* BOTÃO */}
                                <button
                                    onClick={(e) => {
                                        e.stopPropagation();
                                        navigate(`/qrcode/${qr.id}`);
                                    }}
                                    className="
                    w-full py-2 rounded-xl
                    border border-indigo-500/60
                    text-indigo-400 text-sm font-medium
                    hover:bg-indigo-600 hover:text-white
                    transition
                  "
                                >
                                    {qr.paid ? "Ver detalhes" : "Finalizar pagamento"}
                                </button>
                            </div>
                        ))}
                    </div>
                ) : (
                    <div className="mt-24 text-center text-slate-400">
                        <p className="text-lg mb-2">Nenhum QR Code criado ainda</p>
                        <Link
                            to="/create"
                            className="inline-flex mt-4 px-6 py-3 bg-indigo-600 hover:bg-indigo-500 rounded-xl font-semibold transition"
                        >
                            Criar primeiro QR Code
                        </Link>
                    </div>
                )}
            </main>
        </div>
    );
}
