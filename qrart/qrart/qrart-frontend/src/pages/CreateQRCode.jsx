import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { qrcodeService } from "../services/qrcodeService";

export default function CreateQRCode() {
    const [title, setTitle] = useState("");
    const [text, setText] = useState("");
    const [logoFile, setLogoFile] = useState(null);
    const [error, setError] = useState("");
    const [loading, setLoading] = useState(false);

    const navigate = useNavigate();

    const handleFileChange = (e) => {
        setLogoFile(e.target.files[0]);
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        setError("");
        setLoading(true);

        try {
            const qrcode = await qrcodeService.create(title, text, logoFile);
            navigate(`/qrcode/${qrcode.id}`);
        } catch (err) {
            setError(
                err.response?.data?.message ||
                "Erro ao criar QR Code"
            );
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className="min-h-screen bg-[#020617] text-white px-6 py-8">
            <div className="max-w-3xl mx-auto">
                {/* Header */}
                <div className="mb-8">
                    <h1 className="text-3xl font-bold mb-1">
                        Criar QR Code
                    </h1>
                    <p className="text-slate-400">
                        Preencha as informações abaixo para gerar seu QR Code
                    </p>
                </div>

                {/* Card */}
                <div className="bg-slate-900/60 border border-white/10 rounded-2xl p-6">
                    {error && (
                        <div className="mb-5 px-4 py-3 rounded-xl bg-red-500/10 text-red-400 text-sm">
                            {error}
                        </div>
                    )}

                    <form onSubmit={handleSubmit} className="space-y-6">
                        {/* Título */}
                        <div>
                            <label className="block text-sm font-medium text-slate-300 mb-2">
                                Título
                            </label>
                            <input
                                value={title}
                                onChange={(e) => setTitle(e.target.value)}
                                required
                                placeholder="Ex: Meu Instagram"
                                className="w-full px-4 py-3 rounded-xl bg-slate-800 border border-white/10 focus:outline-none focus:border-indigo-500"
                            />
                        </div>

                        {/* Texto */}
                        <div>
                            <label className="block text-sm font-medium text-slate-300 mb-2">
                                URL ou Texto
                            </label>
                            <textarea
                                value={text}
                                onChange={(e) => setText(e.target.value)}
                                required
                                rows={4}
                                placeholder="https://meusite.com"
                                className="w-full px-4 py-3 rounded-xl bg-slate-800 border border-white/10 focus:outline-none focus:border-indigo-500 resize-none"
                            />
                        </div>

                        {/* Upload */}
                        <div>
                            <label className="block text-sm font-medium text-slate-300 mb-2">
                                Logo (opcional)
                            </label>

                            <label
                                htmlFor="logo"
                                className="
                                    flex flex-col items-center justify-center
                                    h-40 rounded-xl
                                    border-2 border-dashed border-white/10
                                    bg-black/30
                                    cursor-pointer
                                    hover:border-indigo-500/50
                                    transition
                                "
                            >
                                <input
                                    id="logo"
                                    type="file"
                                    accept="image/*"
                                    onChange={handleFileChange}
                                    className="hidden"
                                />

                                {logoFile ? (
                                    <p className="text-sm text-indigo-400">
                                        {logoFile.name}
                                    </p>
                                ) : (
                                    <>
                                        <p className="text-sm text-slate-400">
                                            Clique para enviar uma imagem
                                        </p>
                                        <p className="text-xs text-slate-500 mt-1">
                                            PNG ou JPG
                                        </p>
                                    </>
                                )}
                            </label>
                        </div>

                        {/* Actions */}
                        <div className="flex gap-4 pt-4">
                            <button
                                type="button"
                                onClick={() => navigate("/dashboard")}
                                className="
                                    flex-1 py-3 rounded-xl
                                    bg-slate-800 hover:bg-slate-700
                                    transition font-medium
                                "
                            >
                                Cancelar
                            </button>

                            <button
                                type="submit"
                                disabled={loading}
                                className="
                                    flex-1 py-3 rounded-xl
                                    bg-indigo-600 hover:bg-indigo-500
                                    transition font-semibold
                                    disabled:opacity-50
                                "
                            >
                                {loading ? "Criando..." : "Criar QR Code"}
                            </button>
                        </div>
                    </form>
                </div>

                {/* Info */}
                <div className="mt-6 bg-indigo-500/10 border border-indigo-500/20 rounded-xl p-4 text-sm text-indigo-300">
                    Após criar o QR Code, será necessário realizar o pagamento
                    para gerar e baixar a imagem final.
                </div>
            </div>
        </div>
    );
}
