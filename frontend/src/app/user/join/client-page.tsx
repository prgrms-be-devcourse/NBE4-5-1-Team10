"use client";
import { Button } from "@/components/ui/button";
import { Card } from "@/components/ui/card";
import {
  Dialog,
  DialogContent,
  DialogDescription,
  DialogFooter,
  DialogHeader,
  DialogTitle,
} from "@/components/ui/dialog";
import { Input } from "@/components/ui/input";
import { useRouter } from "next/navigation";
import { useState } from "react";
import { toast } from "sonner";

export default function ClientPage() {
  const router = useRouter();
  const [completeModalOpen, setCompleteModalOpen] = useState(false);

  async function signUp(e: React.FormEvent<HTMLFormElement>) {
    e.preventDefault();

    const form = e.target as HTMLFormElement;

    const username = form.username.value;
    const email = form.email.value;
    const address = form.address.value;
    const password = form.password.value;

    const signUpRes = await fetch("/api/user/join", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify({ username, email, address, password }),
    });

    if (!signUpRes.ok) {
      const errorResponse = await signUpRes.json();
      toast.error("로그인 실패", {
        description: errorResponse.error || "회원가입 실패",
      });
      return;
    }

    setCompleteModalOpen(true);
  }

  return (
    <div className="flex flex-col justify-center items-center w-full h-screen bg-gray-100 px-4">
      <Card className="w-full max-w-md p-8 shadow-sm">
        <h2 className="text-2xl font-semibold text-center mb-6">회원가입</h2>

        <form onSubmit={signUp} className="flex flex-col gap-4">
          <Input type="text" name="username" placeholder="사용자명" required />
          <Input type="email" name="email" placeholder="이메일" required />
          <Input type="text" name="address" placeholder="주소" required />
          <Input
            type="password"
            name="password"
            placeholder="패스워드"
            required
          />

          <Button type="submit" className="mt-4">
            가입하기
          </Button>
        </form>
      </Card>
      <Dialog open={completeModalOpen} onOpenChange={setCompleteModalOpen}>
        <DialogContent className="sm:max-w-md" aria-describedby="">
          <DialogHeader>
            <DialogTitle>회원 가입 완료</DialogTitle>
            <DialogDescription>
              회원 가입이 완료되었습니다. 로그인 후 이용해주세요.
            </DialogDescription>
          </DialogHeader>

          <DialogFooter>
            <Button
              onClick={(e) => {
                e.preventDefault();
                setCompleteModalOpen(false);
                router.replace("/");
              }}
            >
              로그인 하러 가기
            </Button>
          </DialogFooter>
        </DialogContent>
      </Dialog>
    </div>
  );
}
