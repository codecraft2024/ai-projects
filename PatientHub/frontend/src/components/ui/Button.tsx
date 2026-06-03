import { Link } from "@/i18n/navigation";
import { cn } from "@/utils/cn";

type ButtonVariant = "primary" | "secondary" | "outline" | "whatsapp" | "ghost";
type ButtonSize = "sm" | "md" | "lg";

type ButtonBaseProps = {
  variant?: ButtonVariant;
  size?: ButtonSize;
  className?: string;
  children: React.ReactNode;
};

type ButtonAsButton = ButtonBaseProps &
  React.ButtonHTMLAttributes<HTMLButtonElement> & { href?: undefined };

type ButtonAsLink = ButtonBaseProps & {
  href: string;
  onClick?: () => void;
  target?: string;
  rel?: string;
};

type ButtonProps = ButtonAsButton | ButtonAsLink;

const variantStyles: Record<ButtonVariant, string> = {
  primary:
    "bg-brand text-white shadow-brand hover:bg-brand-dark focus-visible:ring-brand",
  secondary:
    "bg-slate-900 text-white hover:bg-slate-800 focus-visible:ring-slate-600",
  outline:
    "border-2 border-brand text-brand bg-white hover:bg-brand-soft focus-visible:ring-brand",
  whatsapp:
    "bg-[#25D366] text-white shadow-lg shadow-green-600/20 hover:bg-[#20BD5A] focus-visible:ring-green-500",
  ghost: "text-slate-700 hover:bg-muted focus-visible:ring-slate-400",
};

const sizeStyles: Record<ButtonSize, string> = {
  sm: "px-3 py-2 text-sm min-h-[40px]",
  md: "px-5 py-2.5 text-sm min-h-[44px]",
  lg: "px-7 py-3.5 text-base min-h-[48px]",
};

export function Button({
  variant = "primary",
  size = "md",
  className,
  children,
  ...props
}: ButtonProps) {
  const classes = cn(
    "inline-flex items-center justify-center gap-2 rounded-xl font-semibold transition-all duration-200 focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-offset-2",
    variantStyles[variant],
    sizeStyles[size],
    className,
  );

  if ("href" in props && props.href) {
    const { href, ...rest } = props;
    if (href.startsWith("http")) {
      return (
        <a href={href} className={classes} target="_blank" rel="noopener noreferrer" {...rest}>
          {children}
        </a>
      );
    }
    return (
      <Link href={href} className={classes} {...rest}>
        {children}
      </Link>
    );
  }

  const { type = "button", ...rest } = props as ButtonAsButton;
  return (
    <button type={type} className={classes} {...rest}>
      {children}
    </button>
  );
}
