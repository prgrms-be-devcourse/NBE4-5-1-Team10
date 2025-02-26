"use client";

import { LoginUserContext, useLoginUser, User } from "@/stores/auth/auth-store";
import React, { useEffect } from "react";

interface Props {
  children: React.ReactNode;
  userFromServer?: User | null;
}

export default function AuthProvider({ children, userFromServer }: Props) {
  const loginUserState = useLoginUser();
  const { setLoginUser, removeLoginUser } = loginUserState;

  useEffect(() => {
    if (userFromServer && userFromServer.id !== 0) {
      setLoginUser(userFromServer);
    } else {
      removeLoginUser();
    }
  }, [userFromServer]);

  return (
    <LoginUserContext.Provider value={loginUserState}>
      {children}
    </LoginUserContext.Provider>
  );
}
