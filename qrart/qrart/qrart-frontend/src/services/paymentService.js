import api from "./api";

export const paymentService = {
    createCheckout: async (qrCodeId) => {
        const response = await api.post(
            `/payments/create-checkout`,
            null,
            {
                params: { qrCodeId }
            }
        );
        return response.data;
    }
};
