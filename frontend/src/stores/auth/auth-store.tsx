"use client";

import { createContext, useState } from "react";

type User = {
  id: number;
  username: string;
  email: string;
};

export const LoginUserContext = createContext<{
  loginUser: User;
  setLoginUser: (user: User) => void;
  removeLoginUser: () => void;
  isLogin: boolean;
  isLoginUserPending: boolean;
  setAnonymousUser: () => void;
}>({
  loginUser: createEmptyUser(),
  setLoginUser: () => {},
  removeLoginUser: () => {},
  isLogin: false,
  isLoginUserPending: true,
  setAnonymousUser: () => {},
});

function createEmptyUser(): User {
  return {
    id: 0,
    username: "",
    email: "",
  };
}

export function useLoginUser() {
  const [isLoginUserPending, setLoginUserPending] = useState(true);
  const [loginUser, _setLoginUser] = useState<User>(createEmptyUser());

  const removeLoginUser = () => {
    _setLoginUser(createEmptyUser()); // 로그아웃 시 초기 상태로 되돌리기
    setLoginUserPending(false);
  };

  const setLoginUser = (user: User) => {
    _setLoginUser(user);
    setLoginUserPending(false);
  };

  const setAnonymousUser = () => {
    setLoginUserPending(false);
  };

  const isLogin = loginUser.id !== 0;

  return {
    loginUser,
    removeLoginUser,
    isLogin,
    isLoginUserPending,
    setLoginUser,
    setAnonymousUser,
  };
}
