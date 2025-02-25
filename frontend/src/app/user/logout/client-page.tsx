"use client";
import { Button } from "@/components/ui/button";
import { Card } from "@/components/ui/card";
import { useRouter } from "next/navigation";
import { useContext } from "react";
import { LoginUserContext } from "@/stores/auth/auth-store";

export default function ClientPage() {
  const router = useRouter();
  const { removeLoginUser } = useContext(LoginUserContext); // removeLoginUser 불러오기

  async function logout() {
    // 쿠키에서 accessToken 삭제
    document.cookie = "accessToken=; expires=Thu, 01 Jan 1970 00:00:00 GMT; path=/;";

    // 로컬 스토리지의 토큰 삭제 (필요 시)
    localStorage.removeItem("accessToken");

    removeLoginUser(); // 로그인 상태를 초기 상태로 되돌림

    alert("로그아웃 성공");
    router.replace("/"); // 홈페이지로 리다이렉트
  }

  return (
    <div className="flex flex-col justify-center items-center w-full h-screen bg-gray-50 px-4">
      <Card className="w-full max-w-md p-8 shadow-sm">
        <h2 className="text-2xl font-semibold text-center mb-6">로그아웃</h2>
        <Button onClick={logout} className="mt-4">
          로그아웃
        </Button>
      </Card>
    </div>
  );
}