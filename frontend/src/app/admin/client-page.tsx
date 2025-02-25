"use client";

import { Card } from "@/components/ui/card";
import { LoginUserContext } from "@/stores/auth/auth-store";
import { use } from "react";

export default function ClientPage() {
  const { isLogin, loginUser } = use(LoginUserContext);
  return (
    <div className="flex flex-col justify-center items-center w-full min-h-screen bg-gray-50">
      <h2 className="text-2xl font-semibold text-center mb-6">관리자 페이지</h2>
      {isLogin && <div>{loginUser.username}님 환영합니다.</div>}
    </div>
  );
}
