"use client";
import { Button } from "@/components/ui/button";
import { Card } from "@/components/ui/card";
import { Input } from "@/components/ui/input";
import client from "@/lib/backend/client";
import { LoginUserContext } from "@/stores/auth/auth-store";
import { useRouter } from "next/navigation";
import { use } from "react";

export default function ClientPage() {
  const router = useRouter();
  const { setLoginUser } = use(LoginUserContext);

  async function login(e: React.FormEvent<HTMLFormElement>) {
    e.preventDefault();

    const form = e.target as HTMLFormElement;

    const email = form.email.value;
    const password = form.password.value;

    if (email.trim().length === 0) {
      alert("이메일을 입력해주세요.");
      return;
    }

    if (password.trim().length === 0) {
      alert("패스워드를 입력해주세요.");
      return;
    }

    // const loginResponse = await client.POST("/api/v1/user/login", {
    //   body: {
    //     email,
    //     password,
    //   },
    // });
    const loginResponse = await fetch(
      "http://localhost:8080/api/v1/user/login",
      {
        method: "POST",
        body: JSON.stringify({
          email,
          password,
        }),
      }
    );

    if (!loginResponse.ok) {
      alert("로그인에 실패했습니다.");
      return;
    }

    const loginData = (await loginResponse.json()).data || { access: "" };
    const accessToken = loginData["access"];

    const userResponse = await client.GET("/api/v1/user", {
      headers: {
        Authorization: "Bearer " + accessToken,
      },
      credentials: "include",
    });

    if (userResponse.error) {
      alert(userResponse["error"]["msg"]);
      return;
    }

    setLoginUser(userResponse.data.data!!);
    localStorage.setItem("accessToken", accessToken);
    router.replace("/");
  }

  return (
    <>
      <div className="flex flex-col justify-center items-center w-full h-screen bg-gray-50 px-4">
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

            <Button type="submit" className="mt-4">
              로그인
            </Button>
          </form>
        </Card>
      </div>
    </>
  );
}
