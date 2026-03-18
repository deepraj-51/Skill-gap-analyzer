import api from './api';

export const analysisService = {
    analyzeGap: async (userId, roleId) => {
        const response = await api.post('/analysis/analyze', null, {
            params: { userId, roleId },
        });
        return response.data;
    },

    getAnalysisHistory: async (userId) => {
        const response = await api.get(`/analysis/history/${userId}`);
        return response.data;
    },

    getAllRoles: async () => {
        const response = await api.get('/roles');
        return response.data;
    },

    getRoleById: async (roleId) => {
        const response = await api.get(`/roles/${roleId}`);
        return response.data;
    },

    getRoleRequirements: async (roleId) => {
        const response = await api.get(`/roles/${roleId}/requirements`);
        return response.data;
    },
};
