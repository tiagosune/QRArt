export default function AuthLayout({ children }) {
    return (
        <div className="min-h-screen flex items-center justify-center px-4 bg-slate-100 dark:bg-[#020617] overflow-hidden">
            <div className="w-full max-w-[420px]">
                {children}
            </div>
        </div>
    );
}
