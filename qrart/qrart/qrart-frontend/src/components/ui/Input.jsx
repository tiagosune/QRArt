export default function Input({
                                  label,
                                  value,
                                  onChange,
                                  type = "text",
                                  placeholder,
                                  required = false,
                              }) {
    return (
        <div className="flex flex-col gap-1">
            <label className="text-xs font-semibold text-slate-600 dark:text-slate-300">
                {label}
            </label>

            <input
                type={type}
                value={value}
                onChange={onChange}
                placeholder={placeholder}
                required={required}
                className="
          h-12 rounded-xl px-4 transition
          bg-white text-slate-900 border border-slate-300
          placeholder-slate-400
          focus:outline-none focus:border-indigo-500
          focus:ring-2 focus:ring-indigo-500/30

          dark:bg-[#020617] dark:text-white
          dark:border-slate-700 dark:placeholder-slate-500
        "
            />
        </div>
    );
}
