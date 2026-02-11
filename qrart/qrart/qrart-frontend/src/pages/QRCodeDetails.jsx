import { useEffect, useState } from "react";
import { useParams, useNavigate } from "react-router-dom";
import { qrcodeService } from "../services/qrcodeService";
import { paymentService } from "../services/paymentService";

export default function QRCodeDetails() {
    const { id } = useParams();
    const navigate = useNavigate();

    const [qrcode, setQrcode] = useState(null);
    const [loading, setLoading] = useState(true);
    const [editing, setEditing] = useState(false);

    const [title, setTitle] = useState("");
    const [text, setText] = useState("");

    useEffect(() => {
        loadQRCode();
        // eslint-disable-next-line
    }, [id]);

    const loadQRCode = async () => {
        try {
            const data = await qrcodeService.getById(id);
            setQrcode(data);
            setTitle(data.title || "");
            setText(data.text || "");
        } catch (err) {
            console.error(err);
        } finally {
            setLoading(false);
        }
    };

    const handleDownload = () => {
        const link = document.createElement("a");
        link.href = `http://localhost:8080${qrcode.imgPath}`;
        link.download = `qrcode-${qrcode.id}.png`;
        document.body.appendChild(link);
        link.click();
        document.body.removeChild(link);
    };

    const handleUpdate = async () => {
        try {
            await qrcodeService.update(qrcode.id, title, text);
            setEditing(false);
            loadQRCode();
        } catch (err) {
            alert("Erro ao atualizar QR Code");
        }
    };

    const handleDelete = async () => {
        const confirmDelete = window.confirm(
            "Tem certeza que deseja excluir este QR Code?\nEssa ação não pode ser desfeita."
        );

        if (!confirmDelete) return;

        try {
            await qrcodeService.delete(qrcode.id);
            navigate("/dashboard", { replace: true });
        } catch (err) {
            alert("Erro ao excluir QR Code");
        }
    };

    const handlePayment = async () => {
        try {
            const { checkoutUrl } = await paymentService.createCheckout(qrcode.id);
            window.location.href = checkoutUrl;
        } catch {
            alert("Erro ao iniciar pagamento");
        }
    };

    if (loading) {
        return (
            <div className="h-screen flex items-center justify-center bg-[#020617] text-white">
                Carregando...
            </div>
        );
    }

    if (!qrcode) {
        return (
            <div className="h-screen flex items-center justify-center bg-[#020617] text-white">
                QR Code não encontrado
            </div>
        );
    }

    return (
        <div className="h-screen bg-[#020617] text-white px-6 py-6 overflow-hidden">
            {/* Header */}
            <div className="max-w-6xl mx-auto flex items-center justify-between mb-6">
                <button
                    onClick={() => navigate("/dashboard")}
                    className="text-slate-400 hover:text-white transition"
                >
                    ← Voltar
                </button>

                {!editing && (
                    <div className="flex gap-3">
                        <button
                            onClick={() => setEditing(true)}
                            className="px-4 py-2 rounded-lg bg-slate-800 hover:bg-slate-700 transition"
                        >
                            Editar
                        </button>

                        <button
                            onClick={handleDelete}
                            className="px-4 py-2 rounded-lg bg-red-600/80 hover:bg-red-600 transition font-semibold"
                        >
                            Excluir
                        </button>
                    </div>
                )}
            </div>

            {/* Content */}
            <div className="max-w-6xl mx-auto h-[calc(100%-60px)] grid grid-cols-1 md:grid-cols-2 gap-6">
                {/* Preview */}
                <div className="bg-slate-900/60 border border-white/10 rounded-2xl p-6 flex flex-col justify-between">
                    <div className="flex items-center justify-center bg-black/30 rounded-xl h-[300px]">
                        {qrcode.paid && qrcode.imgPath ? (
                            <img
                                src={`http://localhost:8080${qrcode.imgPath}`}
                                alt={qrcode.title}
                                className="max-h-[240px] object-contain"
                            />
                        ) : (
                            <span className="text-slate-400">
                                Pagamento pendente
                            </span>
                        )}
                    </div>

                    {qrcode.paid && (
                        <button
                            onClick={handleDownload}
                            className="mt-6 w-full py-3 rounded-xl bg-emerald-600 hover:bg-emerald-500 transition font-semibold"
                        >
                            Baixar QR Code
                        </button>
                    )}
                </div>

                {/* Details */}
                <div className="bg-slate-900/60 border border-white/10 rounded-2xl p-6 flex flex-col justify-between">
                    {!editing ? (
                        <>
                            <div>
                                <span
                                    className={`inline-block mb-3 px-3 py-1 rounded-full text-xs font-semibold ${
                                        qrcode.paid
                                            ? "bg-emerald-500/20 text-emerald-400"
                                            : "bg-amber-500/20 text-amber-400"
                                    }`}
                                >
                                    {qrcode.paid ? "Pago" : "Pendente"}
                                </span>

                                <h1 className="text-2xl font-bold mb-3">
                                    {qrcode.title || `QR Code #${qrcode.id}`}
                                </h1>

                                <p className="text-slate-400 break-all">
                                    {qrcode.text}
                                </p>
                            </div>

                            {!qrcode.paid && (
                                <div className="mt-6">
                                    <button
                                        onClick={handlePayment}
                                        className="w-full py-3 rounded-xl bg-indigo-600 hover:bg-indigo-500 transition font-semibold"
                                    >
                                        Finalizar pagamento
                                    </button>
                                </div>
                            )}
                        </>
                    ) : (
                        <>
                            <div className="space-y-4">
                                <input
                                    value={title}
                                    onChange={(e) => setTitle(e.target.value)}
                                    className="w-full p-3 rounded-xl bg-slate-800 border border-white/10 focus:outline-none focus:border-indigo-500"
                                    placeholder="Título"
                                />

                                <textarea
                                    value={text}
                                    onChange={(e) => setText(e.target.value)}
                                    rows={4}
                                    className="w-full p-3 rounded-xl bg-slate-800 border border-white/10 focus:outline-none focus:border-indigo-500"
                                    placeholder="Conteúdo"
                                />
                            </div>

                            <div className="flex gap-3 mt-6">
                                <button
                                    onClick={() => setEditing(false)}
                                    className="w-1/2 py-2 rounded-xl bg-slate-700 hover:bg-slate-600 transition"
                                >
                                    Cancelar
                                </button>

                                <button
                                    onClick={handleUpdate}
                                    className="w-1/2 py-2 rounded-xl bg-indigo-600 hover:bg-indigo-500 transition font-semibold"
                                >
                                    Salvar
                                </button>
                            </div>
                        </>
                    )}
                </div>
            </div>
        </div>
    );
}
