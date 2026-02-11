import { useEffect, useState } from "react";
import { adminService } from "../../services/admin";
import AdminNavbar from "../../components/layout/AdminNavbar";

export default function AdminQRCodes() {
    const [qrcodes, setQRCodes] = useState([]);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        loadQRCodes();
    }, []);

    const loadQRCodes = async () => {
        try {
            const data = await adminService.getQRCodes();
            setQRCodes(data);
        } catch (err) {
            console.error("Erro ao carregar QR Codes", err);
        } finally {
            setLoading(false);
        }
    };

    const handleDelete = async (id) => {
        if (!window.confirm("Deseja excluir este QR Code?")) return;

        await adminService.deleteQRCode(id);
        setQRCodes(prev =>
            prev.map(qr =>
                qr.id === id ? { ...qr, deleted: true } : qr
            )
        );
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
            <AdminNavbar />

            <main className="max-w-7xl mx-auto px-6 py-10">
                <h1 className="text-3xl font-bold mb-2">QR Codes do Sistema</h1>
                <p className="text-slate-400 mb-8">
                    Visualize e gerencie todos os QR Codes
                </p>

                <div className="overflow-x-auto">
                    <table className="w-full text-sm border border-white/10 rounded-xl overflow-hidden">
                        <thead className="bg-slate-900">
                        <tr>
                            <th className="p-4">ID</th>
                            <th className="p-4">Título</th>
                            <th className="p-4">Usuário</th>
                            <th className="p-4">Status</th>
                            <th className="p-4">Ações</th>
                        </tr>
                        </thead>
                        <tbody>
                        {qrcodes.map(qr => (
                            <tr key={qr.id} className="border-t border-white/10">
                                <td className="p-4">{qr.id}</td>
                                <td className="p-4">{qr.title}</td>
                                <td className="p-4 text-slate-400">{qr.userEmail}</td>
                                <td className="p-4">
                                    {qr.deleted ? "🗑️ Deletado" : qr.paid ? "✅ Pago" : "⏳ Pendente"}
                                </td>
                                <td className="p-4">
                                    {!qr.deleted && (
                                        <button
                                            onClick={() => handleDelete(qr.id)}
                                            className="text-red-400 hover:text-red-300"
                                        >
                                            Excluir
                                        </button>
                                    )}
                                </td>
                            </tr>
                        ))}
                        </tbody>
                    </table>
                </div>
            </main>
        </div>
    );
}
