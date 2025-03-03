
import type { Metadata } from "next";
import "./globals.css";

import ClientLayout from "./client-layout";

import { config } from "@fortawesome/fontawesome-svg-core";
import localFont from "next/font/local";
import AuthProvider from "@/providers/auth.provider";
import { Toaster } from "@/components/ui/sonner";

config.autoAddCss = false;

export const metadata: Metadata = {
  title: "Create Next App",
  description: "Generated by create next app",
};

const pretendard = localFont({
  src: "./../../node_modules/pretendard/dist/web/variable/woff2/PretendardVariable.woff2",
  display: "swap",
  weight: "45 920",
  variable: "--font-pretendard",
});

export default async function RootLayout({
  children,
}: Readonly<{
  children: React.ReactNode;
}>) {
  return (
    <html
      lang="en"
      className={`${pretendard.variable}`}
      suppressHydrationWarning
    >
    <body className={`min-h-[100dvh] flex flex-col ${pretendard.className}`}>
          <ClientLayout>{children}</ClientLayout>
        <Toaster position="top-right" richColors />
      </body>
    </html>
  );
}