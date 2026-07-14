"use client";

import { motion } from "framer-motion";
import { ArrowRight, Lock } from "lucide-react";
import Link from "next/link";
import { Badge } from "@/components/ui/badge";
import { buttonVariants } from "@/components/ui/button";
import { Card, CardDescription, CardFooter, CardHeader, CardTitle } from "@/components/ui/card";
import type { CatalogItem } from "@/types";
import { cn } from "@/lib/utils";

export function CatalogGrid({ items }: { items: CatalogItem[] }) {
  return (
    <div className="grid gap-5 sm:grid-cols-2 xl:grid-cols-3">
      {items.map((item, index) => (
        <motion.div
          key={item.id}
          initial={{ opacity: 0, y: 12 }}
          animate={{ opacity: 1, y: 0 }}
          transition={{ delay: index * 0.05 }}
          whileHover={item.status === "ready" ? { y: -3 } : undefined}
        >
          <Card className="group h-full overflow-hidden rounded-2xl border-border/60 bg-card/80 shadow-sm backdrop-blur transition-shadow hover:shadow-md">
            <div className="h-1 bg-gradient-to-r from-sky-500 via-violet-500 to-emerald-500 opacity-0 transition-opacity group-hover:opacity-100" />
            <CardHeader>
              <div className="flex items-start justify-between gap-3">
                <CardTitle className="text-lg">{item.name}</CardTitle>
                {item.status === "ready" ? (
                  <Badge className="rounded-full bg-emerald-500/15 text-emerald-700 dark:text-emerald-300">
                    Ready
                  </Badge>
                ) : (
                  <Badge variant="secondary" className="rounded-full">
                    Coming Soon
                  </Badge>
                )}
              </div>
              <CardDescription className="leading-relaxed">{item.description}</CardDescription>
            </CardHeader>
            <CardFooter>
              {item.status === "ready" ? (
                <Link
                  href={item.href}
                  className={cn(buttonVariants(), "w-full rounded-xl")}
                >
                  Open
                  <ArrowRight className="size-4" />
                </Link>
              ) : (
                <span
                  className={cn(
                    buttonVariants({ variant: "outline" }),
                    "pointer-events-none w-full rounded-xl opacity-60",
                  )}
                >
                  <Lock className="size-4" />
                  Unavailable
                </span>
              )}
            </CardFooter>
          </Card>
        </motion.div>
      ))}
    </div>
  );
}
