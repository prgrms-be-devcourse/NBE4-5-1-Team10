"use client";

import { Button } from "@/components/ui/button";
import { Card } from "@/components/ui/card";
import { Input } from "@/components/ui/input";
import Loading from "@/components/utils/loading";
import { Toaster } from "@/components/ui/sonner";
import client from "@/lib/backend/client";
import { LoginUserContext } from "@/stores/auth/auth-store";
import { useRouter } from "next/navigation";
import { use, useState } from "react";
import { toast } from "sonner";

export default function ClientPage() {
  const router = useRouter();
  const { setLoginUser } = use(LoginUserContext);
  const [isLoading, setIsLoading] = useState(false);

  function showErrorToast(message: string) {
    toast.error("로그인 실패", {
      description: message,
    });
  }

  async function login(e: React.FormEvent<HTMLFormElement>) {
    e.preventDefault();
    setIsLoading(true);

    const form = e.target as HTMLFormElement;
    const email = form.email.value;
    const password = form.password.value;

    if (!email.trim() || !password.trim()) {
      showErrorToast("이메일과 비밀번호를 입력해주세요.");
      setIsLoading(false);
      return;
    }

    try {
      const loginRes = await fetch("/api/user/login", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ email, password }),
        credentials: "include",
      });

      if (!loginRes.ok) {
        throw new Error("로그인 실패");
      }

      const res = await fetch("/api/user", {
        method: "GET",
        headers: { "Content-Type": "application/json" },
        credentials: "include",
      });

      if (!res.ok) {
        throw new Error("사용자 정보를 불러오지 못했습니다.");
      }

      const data = await res.json();
      const user = data.data!!;

      setLoginUser(user);
      router.replace("/");
    } catch (error) {
      showErrorToast("로그인 실패");
    } finally {
      setIsLoading(false);
    }
  }

  if (isLoading) {
    return <Loading message="로그인 중..." />;
  }

  return (
    <>
      <div className="flex flex-col justify-center items-center w-full min-h-screen bg-gray-100 px-4">
        <Card className="w-full max-w-md p-8 shadow-sm">
          <h2 className="text-2xl font-semibold text-center mb-6">로그인</h2>

          <form onSubmit={login} className="flex flex-col gap-4">
            <Input
              type="text"
              name="email"
              placeholder="아이디(이메일) 입력"
              required
            />
            <Input
              type="password"
              name="password"
              placeholder="패스워드 입력"
              required
            />

            <Button
              type="submit"
              className="w-full mt-4 bg-black text-white hover:bg-black/90"
              disabled={isLoading}
            >
              {isLoading ? "로그인 중..." : "로그인"}
            </Button>
          </form>
          <Button
            className="w-full mt-4 bg-black text-white hover:bg-black/90"
            onClick={() => router.push("/user/join")}
          >
            회원가입
          </Button>
        </Card>
      </div>
    </>
  );
}
