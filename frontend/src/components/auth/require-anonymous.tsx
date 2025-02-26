"use client";

import { Button } from "@/components/ui/button";
import { Card } from "@/components/ui/card";
import { LoginUserContext } from "@/stores/auth/auth-store";
import Link from "next/link";
import { use } from "react";
import Loading from "../utils/loading";

export default function RequireAnonymous({
  children,
}: {
  children: React.ReactNode;
}) {
  const { isLogin } = use(LoginUserContext);

  if (isLogin)
    return (
      <div className="flex justify-center items-center w-full min-h-screen px-8 py-12 bg-gray-50">
        <Card className="p-8 bg-white shadow-sm max-w-md w-full">
          <div className="text-center">
            <p className="text-lg text-muted-foreground font-medium">
              해당 페이지는 로그아웃 후 이용할 수 있습니다.
            </p>
            <Button className="mt-6" asChild>
              <Link href="/">메인으로 돌아가기</Link>
            </Button>
          </div>
        </Card>
      </div>
    );

  return <>{children}</>;
}
