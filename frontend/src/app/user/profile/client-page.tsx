"use client";

import { Card } from "@/components/ui/card";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { useState } from "react";
import Image from "next/image";
import { useRouter } from "next/navigation";
import { LoginUserContext } from "@/stores/auth/auth-store";
import { use } from "react";

export default function ClientPage() {
  const { loginUser, removeLoginUser } = use(LoginUserContext);
  const router = useRouter();

  const handleLogout = async () => {
    try {
      const res = await fetch("/api/user/logout", {
        method: "POST",
        credentials: "include",
      });
      if (!res.ok) {
        return;
      }
      removeLoginUser();
      router.replace("/");
    } catch (err) {
      return;
    }
  };

  return (
    <div className="flex justify-center items-center w-full min-h-screen px-8 py-12 bg-gray-100">
      <Card className="w-full max-w-screen-md p-8 bg-white shadow-md">
        <h2 className="text-2xl font-semibold text-center mb-6">내 정보</h2>

        <div className="flex flex-col items-center gap-4">
          <p className="text-lg font-semibold">{loginUser.username}</p>
          <p className="text-gray-500">{loginUser.email}</p>
        </div>

        <div className="mt-6 flex flex-col gap-4">
          <Button
            onClick={handleLogout}
            size="lg"
            className="w-full bg-red-500 text-white"
          >
            로그아웃
          </Button>
        </div>
      </Card>
    </div>
  );
}
