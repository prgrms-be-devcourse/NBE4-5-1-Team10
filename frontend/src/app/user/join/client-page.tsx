"use client";
import { Button } from "@/components/ui/button";
import { Card } from "@/components/ui/card";
import { Input } from "@/components/ui/input";
import { useRouter } from "next/navigation";

export default function ClientPage() {
  const router = useRouter();

  async function signup(e: React.FormEvent<HTMLFormElement>) {
    e.preventDefault();

    const form = e.target as HTMLFormElement;

    const username = form.username.value;
    const email = form.email.value;
    const address = form.address.value;
    const password = form.password.value;

    const signupRes = await fetch("/api/user/join", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify({ username, email, address, password }),
    });

    if (!signupRes.ok) {
      const errorResponse = await signupRes.json();
      alert(errorResponse.error || "회원가입 실패");  // 오류 메시지 표시
      return;
    }

    alert("회원가입 성공");
    router.replace("/"); // 홈으로 리다이렉트
  }

  return (
    <div className="flex flex-col justify-center items-center w-full h-screen bg-gray-50 px-4">
      <Card className="w-full max-w-md p-8 shadow-sm">
        <h2 className="text-2xl font-semibold text-center mb-6">회원가입</h2>

        <form onSubmit={signup} className="flex flex-col gap-4">
          <Input type="text" name="username" placeholder="사용자명" required />
          <Input type="email" name="email" placeholder="이메일" required />
          <Input type="text" name="address" placeholder="주소" required />
          <Input type="password" name="password" placeholder="패스워드" required />

          <Button type="submit" className="mt-4">
            가입하기
          </Button>
        </form>
      </Card>
    </div>
  );
}