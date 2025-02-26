"use client";

import { LoginUserContext } from "@/stores/auth/auth-store";
import { use, useEffect, useState } from "react";
import AdminPage from "./admin/page";
import Loading from "@/components/utils/loading";
import LoginPage from "./user/login/page";
import ProductListPage from "./product/list/page";
import ProductClientPage from "./product/list/client-page";

export default function Page() {
  const { isLogin, isAdmin, isLoginUserPending } = use(LoginUserContext);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    if (!isLoginUserPending) {
      setLoading(false);
    }
  }, [isLoginUserPending]);

  if (loading) {
    return <Loading message="페이지를 불러오는 중..." />;
  }

  return (
    <>
      {!isLogin && <LoginPage />}
      {isLogin &&
        (isAdmin ? (
          <AdminPage />
        ) : (
          <ProductClientPage data={null} keyword={""} page={1} pageSize={10} />
        ))}
    </>
  );
}
