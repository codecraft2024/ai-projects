import { Header } from "@/components/layout/Header";
import { Footer } from "@/components/layout/Footer";
import { HomePageExtras } from "@/components/layout/HomePageExtras";

type SiteChromeProps = {
  children: React.ReactNode;
  showFloatingWhatsApp?: boolean;
};

export function SiteChrome({ children, showFloatingWhatsApp = true }: SiteChromeProps) {
  return (
    <>
      <Header />
      <main>{children}</main>
      <Footer />
      {showFloatingWhatsApp && <HomePageExtras />}
    </>
  );
}
