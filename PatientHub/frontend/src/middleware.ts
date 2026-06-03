import createMiddleware from "next-intl/middleware";
import { NextRequest } from "next/server";
import { routing, LOCALE_STORAGE_KEY } from "@/i18n/routing";

const intlMiddleware = createMiddleware(routing);

export default function middleware(request: NextRequest) {
  const response = intlMiddleware(request);

  // Sync preferred locale from localStorage header (set by client) or cookie
  const preferred =
    request.cookies.get("NEXT_LOCALE")?.value ??
    request.cookies.get(LOCALE_STORAGE_KEY)?.value;

  if (preferred && routing.locales.includes(preferred as "en" | "ar")) {
    response.cookies.set("NEXT_LOCALE", preferred, {
      path: "/",
      maxAge: 60 * 60 * 24 * 365,
      sameSite: "lax",
    });
  }

  return response;
}

export const config = {
  matcher: ["/", "/(ar|en)/:path*", "/((?!_next|api|og|.*\\..*).*)"],
};
