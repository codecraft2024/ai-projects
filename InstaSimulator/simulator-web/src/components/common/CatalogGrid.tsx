"use client";

import { motion } from "framer-motion";
import { ArrowRight, Lock } from "lucide-react";
import Link from "next/link";
import { buttonVariants } from "@/components/ui/button";
import type { CatalogItem } from "@/types";
import { cn } from "@/lib/utils";

export function CatalogGrid({ items }: { items: CatalogItem[] }) {
  return (
    <div className="grid gap-4 sm:grid-cols-2 xl:grid-cols-3">
      {items.map((item, index) => (
        <motion.div
          key={item.id}
          initial={{ opacity: 0, y: 10 }}
          animate={{ opacity: 1, y: 0 }}
          transition={{ delay: index * 0.04, duration: 0.35 }}
          whileHover={item.status === "ready" ? { y: -2 } : undefined}
        >
          <article className="panel-glass group flex h-full flex-col overflow-hidden rounded-2xl transition-[border-color,transform] duration-200 hover:border-signal/35">
            <div className="h-px w-full bg-gradient-to-r from-transparent via-signal/50 to-transparent opacity-0 transition-opacity group-hover:opacity-100" />
            <div className="flex flex-1 flex-col p-5">
              <div className="mb-3 flex items-start justify-between gap-3">
                <h3 className="font-heading text-lg font-semibold tracking-tight">{item.name}</h3>
                <span
                  className={cn(
                    "rounded-md px-2 py-0.5 text-[11px] font-medium tracking-wide uppercase",
                    item.status === "ready"
                      ? "bg-emerald-500/12 text-emerald-700 dark:text-emerald-300"
                      : "bg-muted text-muted-foreground",
                  )}
                >
                  {item.status === "ready" ? "Ready" : "Soon"}
                </span>
              </div>
              <p className="mb-5 flex-1 text-sm leading-relaxed text-muted-foreground">
                {item.description}
              </p>
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
            </div>
          </article>
        </motion.div>
      ))}
    </div>
  );
}
