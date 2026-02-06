export default function Card({ children }) {
    return (
        <div
            className="
        w-full
        max-h-[92vh]
        rounded-2xl
        bg-white dark:bg-[#020617]
        border border-slate-200 dark:border-slate-800
        shadow-xl

        px-8 pt-8 pb-6
        flex flex-col
      "
        >
            {children}
        </div>
    );
}
