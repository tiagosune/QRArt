export default function Button({ children, loading }) {
    return (
        <button
            className="
    w-full h-12 rounded-xl font-semibold transition

    bg-indigo-600 text-white
    hover:bg-indigo-500

    disabled:opacity-50
  "
        >

        {loading ? "Entrando..." : children}
        </button>
    );
}
