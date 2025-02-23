"use client";

import Link from "next/link";
import { useRouter } from "next/navigation";

export default function ClientLayout({
  children,
}: Readonly<{
  children: React.ReactNode;
}>) {
  const router = useRouter();

  return (
    <>
      <header className="flex justify-end gap-3 px-4"></header>
      <div className="flex flex-col flex-grow justify-center items-center">
        {children}
      </div>
      <footer className="flex justify-center gap-7 p-4">
        <Link href="/product/list">상품 목록</Link>
      </footer>
    </>
  );
}
