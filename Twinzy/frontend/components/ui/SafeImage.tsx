"use client";

import Image from "next/image";
import { useEffect, useState } from "react";
import { ImageOff } from "lucide-react";
import { cn } from "@/lib/utils";
import { hasValidImageUrl, normalizeImageUrl } from "@/lib/profile-image";

interface SafeImageProps {
  src: string;
  alt: string;
  fill?: boolean;
  width?: number;
  height?: number;
  className?: string;
  sizes?: string;
  priority?: boolean;
  unoptimized?: boolean;
  fallbackSrc?: string;
}

function isRemoteUrl(src: string): boolean {
  return src.startsWith("http://") || src.startsWith("https://");
}

export function SafeImage({
  src,
  alt,
  fill,
  width,
  height,
  className,
  sizes,
  priority,
  unoptimized,
  fallbackSrc,
}: SafeImageProps) {
  const normalizedSrc = normalizeImageUrl(src);
  const normalizedFallback = normalizeImageUrl(fallbackSrc);
  const [failed, setFailed] = useState(false);
  const [activeSrc, setActiveSrc] = useState(normalizedSrc);

  useEffect(() => {
    setActiveSrc(normalizedSrc);
    setFailed(false);
  }, [normalizedSrc]);

  if (!hasValidImageUrl(activeSrc) || failed) {
    return (
      <div
        className={cn(
          "flex items-center justify-center bg-muted/80 text-muted-foreground",
          fill ? "absolute inset-0" : "",
          className,
        )}
        style={!fill ? { width, height } : undefined}
        aria-label={alt}
      >
        <ImageOff className="h-8 w-8 opacity-40" />
      </div>
    );
  }

  const handleError = () => {
    if (hasValidImageUrl(normalizedFallback) && normalizedFallback !== activeSrc) {
      setActiveSrc(normalizedFallback);
      return;
    }
    setFailed(true);
  };

  if (isRemoteUrl(activeSrc) || unoptimized) {
    return (
      // eslint-disable-next-line @next/next/no-img-element
      <img
        src={activeSrc}
        alt={alt}
        className={cn(fill ? "absolute inset-0 h-full w-full object-cover" : "", className)}
        width={fill ? undefined : width}
        height={fill ? undefined : height}
        loading={priority ? "eager" : "lazy"}
        decoding="async"
        onError={handleError}
      />
    );
  }

  return (
    <Image
      src={activeSrc}
      alt={alt}
      fill={fill}
      width={width}
      height={height}
      className={className}
      sizes={sizes}
      priority={priority}
      unoptimized={unoptimized}
      onError={handleError}
    />
  );
}
