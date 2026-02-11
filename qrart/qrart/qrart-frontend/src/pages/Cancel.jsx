import { useNavigate } from 'react-router-dom';

export default function Cancel() {
    const navigate = useNavigate();

    return (
        <div className="min-h-screen flex items-center justify-center bg-gradient-to-br from-amber-50 to-amber-100 px-4">
            <div className="max-w-md w-full text-center animate-scale-in">
                {/* Cancel Icon */}
                <div className="inline-flex items-center justify-center w-24 h-24 bg-amber-500 rounded-full shadow-xl mb-6">
                    <svg className="w-12 h-12 text-white" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                        <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M6 18L18 6M6 6l12 12" />
                    </svg>
                </div>

                {/* Cancel Message */}
                <h1 className="text-4xl font-bold text-amber-900 mb-4">
                    Pagamento Cancelado
                </h1>
                <p className="text-lg text-amber-700 mb-8">
                    Você cancelou o pagamento. Não se preocupe, seus dados foram salvos e você pode tentar novamente quando quiser.
                </p>

                {/* Actions */}
                <div className="flex flex-col sm:flex-row gap-4 justify-center">
                    <button
                        onClick={() => navigate('/')}
                        className="px-8 py-3 bg-white border-2 border-amber-600 text-amber-700 font-semibold rounded-xl hover:bg-amber-50 transition-all"
                    >
                        Voltar ao Início
                    </button>
                    <button
                        onClick={() => navigate(-1)}
                        className="px-8 py-3 bg-amber-600 text-white font-semibold rounded-xl hover:bg-amber-700 hover:shadow-lg transition-all"
                    >
                        Tentar Novamente
                    </button>
                </div>
            </div>
        </div>
    );
}
