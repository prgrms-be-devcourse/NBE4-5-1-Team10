"use client";

import { Button } from "@/components/ui/button";
import { LoginUserContext } from "@/stores/auth/auth-store";
import Link from "next/link";
import { use } from "react";

export default function Page() {
  const { isLogin, loginUser } = use(LoginUserContext);

  return (
    <>
      {!isLogin && (
        <div className="flex flex-grow justify-center items-center">
          <Button>
            <Link href="/user/login">로그인</Link>
          </Button>
        </div>
      )}
      {isLogin && <div>{loginUser.username}님 환영합니다.</div>}
    </>
  );
}
