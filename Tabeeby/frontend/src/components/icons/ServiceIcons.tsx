import type { ServiceIconName } from "@/types/clinic";
import { Bone, Activity, Baby, Trophy, Footprints, Hand } from "lucide-react";

const iconMap = {
  bone: Bone,
  joint: Activity,
  pediatric: Baby,
  sports: Trophy,
  foot: Footprints,
  hand: Hand,
} as const;

export function ServiceIcon({
  name,
  className,
}: {
  name: ServiceIconName;
  className?: string;
}) {
  const Icon = iconMap[name as keyof typeof iconMap];
  return <Icon className={className} />;
}
