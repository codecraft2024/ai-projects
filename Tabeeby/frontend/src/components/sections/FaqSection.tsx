"use client";

import { useState } from "react";
import { useTranslations } from "next-intl";
import { Container } from "@/components/ui/Container";
import { SectionHeading } from "@/components/ui/SectionHeading";
import { cn } from "@/utils/cn";

type FaqItem = { question: string; answer: string };

export function FaqSection() {
  const t = useTranslations("faq");
  const items = t.raw("items") as FaqItem[];
  const [openIndex, setOpenIndex] = useState<number | null>(0);

  return (
    <section id="faq" className="bg-brand-soft/50 py-14 sm:py-20">
      <Container>
        <SectionHeading title={t("title")} />
        <div className="mx-auto mt-10 max-w-3xl space-y-3">
          {items.map((item, index) => {
            const isOpen = openIndex === index;
            return (
              <div key={item.question} className="card-premium overflow-hidden">
                <button
                  type="button"
                  className="flex w-full items-center justify-between gap-4 p-5 text-start font-semibold text-slate-900"
                  onClick={() => setOpenIndex(isOpen ? null : index)}
                  aria-expanded={isOpen}
                >
                  {item.question}
                  <span
                    className={cn(
                      "flex h-8 w-8 shrink-0 items-center justify-center rounded-lg bg-brand text-white transition",
                      isOpen && "rotate-45",
                    )}
                  >
                    +
                  </span>
                </button>
                {isOpen && (
                  <div className="border-t border-[var(--border)] px-5 pb-5 pt-2 text-slate-600">
                    {item.answer}
                  </div>
                )}
              </div>
            );
          })}
        </div>
      </Container>
    </section>
  );
}
