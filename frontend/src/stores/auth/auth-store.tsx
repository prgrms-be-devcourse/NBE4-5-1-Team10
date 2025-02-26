"use client";

import { createContext, useState } from "react";

export type User = {
  id: number;
  username: string;
  email: string;
  role: "ROLE_USER" | "ROLE_ADMIN";
};

export const LoginUserContext = createContext<{
  loginUser: User;
  setLoginUser: (user: User) => void;
  removeLoginUser: () => void;
  isLogin: boolean;
  isLoginUserPending: boolean;
  isAdmin: boolean;
  setAnonymousUser: () => void;
}>({
  loginUser: createEmptyUser(),
  setLoginUser: () => {},
  removeLoginUser: () => {},
  isLogin: false,
  isLoginUserPending: true,
  isAdmin: false,
  setAnonymousUser: () => {},
});

function createEmptyUser(): User {
  return {
    id: 0,
    username: "",
    email: "",
    role: "ROLE_USER",
  };
}

export function useLoginUser() {
  const [isLoginUserPending, setLoginUserPending] = useState(true);
  const [loginUser, _setLoginUser] = useState<User>(createEmptyUser());
  const [isAdmin, _setIsAdmin] = useState(false);

  const removeLoginUser = () => {
    _setLoginUser(createEmptyUser()); // 로그아웃 시 초기 상태로 되돌리기
    setLoginUserPending(false);
    _setIsAdmin(false);
  };

  const setLoginUser = (user: User) => {
    _setLoginUser(user);
    _setIsAdmin(user.role == "ROLE_ADMIN");
    setLoginUserPending(false);
  };

  const setAnonymousUser = () => {
    setLoginUserPending(false);
    _setIsAdmin(false);
  };

  const isLogin = loginUser.id !== 0;

  return {
    loginUser,
    removeLoginUser,
    isLogin,
    isLoginUserPending,
    setLoginUser,
    isAdmin,
    setAnonymousUser,
  };
}
