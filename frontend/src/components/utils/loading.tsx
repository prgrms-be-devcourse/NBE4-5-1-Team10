"use client";

import { LoaderCircle } from "lucide-react";

export default function Loading({ message = "로딩 중..." }) {
  return (
    <div className="flex flex-col justify-center items-center min-h-screen">
      <LoaderCircle className="animate-spin w-10 h-10 text-gray-600" />
      <p className="mt-4 text-gray-500">{message}</p>
    </div>
  );
}
