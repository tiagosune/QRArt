import { useEffect, useState } from "react";
import { useNavigate, useSearchParams } from "react-router-dom";
import api from "../services/api";

export default function Success() {
    const [searchParams] = useSearchParams();
    const navigate = useNavigate();
    const [status, setStatus] = useState("loading");

    const qrCodeId = searchParams.get("qrCodeId");

    useEffect(() => {
        if (!qrCodeId) {
            navigate("/dashboard");
            return;
        }

        let attempts = 0;
        const maxAttempts = 10;

        const checkPayment = async () => {
            try {
                const response = await api.get(`/qrcode/${qrCodeId}`);

                if (response.data.paid) {
                    setStatus("success");
                    setTimeout(() => {
                        navigate(`/qrcode/${qrCodeId}`);
                    }, 1500);
                } else {
                    attempts++;
                    if (attempts < maxAttempts) {
                        setTimeout(checkPayment, 2000);
                    } else {
                        setStatus("timeout");
                    }
                }
            } catch (error) {
                setStatus("error");
            }
        };

        checkPayment();
    }, [qrCodeId, navigate]);

    return (
        <div className="min-h-screen flex items-center justify-center bg-[#020617] text-white">
            <div className="text-center">

                {status === "loading" && (
                    <>
                        <div className="animate-spin h-10 w-10 border-4 border-indigo-500 border-t-transparent rounded-full mx-auto mb-6"></div>
                        <h1 className="text-xl font-semibold">Confirmando pagamento...</h1>
                        <p className="text-slate-400 mt-2">
                            Aguarde enquanto verificamos a transação.
                        </p>
                    </>
                )}

                {status === "success" && (
                    <>
                        <h1 className="text-2xl font-bold text-emerald-400 mb-2">
                            Pagamento confirmado!
                        </h1>
                        <p className="text-slate-400">
                            Redirecionando...
                        </p>
                    </>
                )}

                {status === "timeout" && (
                    <>
                        <h1 className="text-2xl font-bold text-amber-400 mb-2">
                            Estamos finalizando seu pagamento
                        </h1>
                        <p className="text-slate-400 mb-6">
                            Se já foi cobrado, seu QR será liberado em instantes.
                        </p>
                        <button
                            onClick={() => navigate(`/qrcode/${qrCodeId}`)}
                            className="px-6 py-3 bg-indigo-600 hover:bg-indigo-500 rounded-xl font-semibold transition"
                        >
                            Ver QR Code
                        </button>
                    </>
                )}

                {status === "error" && (
                    <>
                        <h1 className="text-2xl font-bold text-red-400 mb-2">
                            Erro ao confirmar pagamento
                        </h1>
                        <button
                            onClick={() => navigate("/dashboard")}
                            className="px-6 py-3 bg-indigo-600 hover:bg-indigo-500 rounded-xl font-semibold transition"
                        >
                            Voltar ao Dashboard
                        </button>
                    </>
                )}

            </div>
        </div>
    );
}
