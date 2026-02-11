import { useEffect, useState } from "react";
import { adminService } from "../../services/admin";
import AdminNavbar from "../../components/layout/AdminNavbar";

export default function AdminUsers() {
    const [users, setUsers] = useState([]);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        loadUsers();
    }, []);

    const loadUsers = async () => {
        const data = await adminService.getUsers();
        setUsers(data);
        setLoading(false);
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
                <h1 className="text-3xl font-bold mb-2">Usuários</h1>
                <p className="text-slate-400 mb-8">
                    Lista de usuários cadastrados
                </p>

                <table className="w-full text-sm border border-white/10 rounded-xl">
                    <thead className="bg-slate-900">
                    <tr>
                        <th className="p-4">ID</th>
                        <th className="p-4">Nome</th>
                        <th className="p-4">Email</th>
                        <th className="p-4">Role</th>
                    </tr>
                    </thead>
                    <tbody>
                    {users.map(u => (
                        <tr key={u.id} className="border-t border-white/10">
                            <td className="p-4">{u.id}</td>
                            <td className="p-4">{u.name}</td>
                            <td className="p-4 text-slate-400">{u.email}</td>
                            <td className="p-4">{u.role}</td>
                        </tr>
                    ))}
                    </tbody>
                </table>
            </main>
        </div>
    );
}
