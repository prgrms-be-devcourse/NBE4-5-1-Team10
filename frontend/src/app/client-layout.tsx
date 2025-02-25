"use client";

import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faHouse } from "@fortawesome/free-solid-svg-icons";
import {
  DropdownMenu,
  DropdownMenuContent,
  DropdownMenuItem,
  DropdownMenuLabel,
  DropdownMenuSeparator,
  DropdownMenuTrigger,
} from "@/components/ui/dropdown-menu";
import { LoginUserContext, useLoginUser } from "@/stores/auth/auth-store";
import Link from "next/link";
import {
  NavigationMenu,
  NavigationMenuItem,
  NavigationMenuLink,
  NavigationMenuList,
} from "@/components/ui/navigation-menu";
import { Button } from "@/components/ui/button";
import { useEffect } from "react";
import client from "@/lib/backend/client";

export default function ClientLayout({
  children,
}: Readonly<{
  children: React.ReactNode;
}>) {
  const {
    setLoginUser,
    isLogin,
    loginUser,
    removeLoginUser,
    isLoginUserPending,
    setAnonymousUser,
  } = useLoginUser();

  const loginUserContextValue = {
    loginUser,
    setLoginUser,
    removeLoginUser,
    isLogin,
    isLoginUserPending,
    setAnonymousUser,
  };

  async function fetchUser() {
    try {
      const res = await fetch("/api/user", {
        method: "GET",
      });
      if (!res.ok) {
        console.error("Fetch user failed:", res.status);
        return;
      }
      const data = await res.json();
      return data.data!!;
    } catch (err) {
      return null;
    }
  }

  useEffect(() => {
    fetchUser()
      .then((userResponse) => {
        if (!userResponse) {
          removeLoginUser();
        } else {
          setLoginUser(userResponse!!);
        }
      })
      .catch(() => removeLoginUser())
      .finally(() => setAnonymousUser());
  }, []);

  return (
    <>
      <LoginUserContext.Provider value={loginUserContextValue}>
        <header className="w-full bg-white shadow-sm py-4 px-8 flex justify-between items-center">
          <Link href="/" className="text-xl font-bold">
            CoffeeProject
          </Link>

          <NavigationMenu>
            <NavigationMenuList className="gap-4">
              <NavigationMenuItem>
                <Link href="/" legacyBehavior passHref>
                  <NavigationMenuLink className="text-sm font-medium">
                    Home
                  </NavigationMenuLink>
                </Link>
              </NavigationMenuItem>

              <NavigationMenuItem>
                <Link href="/product/list" legacyBehavior passHref>
                  <NavigationMenuLink className="text-sm font-medium">
                    상품 목록
                  </NavigationMenuLink>
                </Link>
              </NavigationMenuItem>
            </NavigationMenuList>
          </NavigationMenu>

          <DropdownMenu>
            <DropdownMenuTrigger asChild>
              <Button variant="outline" className="flex items-center gap-2">
                <FontAwesomeIcon icon={faHouse} />
                {isLogin ? loginUser.username : "로그인/회원가입"}
              </Button>
            </DropdownMenuTrigger>

            <DropdownMenuContent align="end">
              {isLogin && (
                <>
                  <DropdownMenuLabel>{loginUser.username}</DropdownMenuLabel>
                  <DropdownMenuSeparator />
                  <DropdownMenuItem asChild>
                    <Link href="/user/profile">내 정보</Link>
                  </DropdownMenuItem>
                  <DropdownMenuItem asChild>
                    <Link href="/cart">장바구니</Link>
                  </DropdownMenuItem>
                  <DropdownMenuItem asChild>
                    <Link href="/order/list">주문 내역</Link>
                  </DropdownMenuItem>
                  <DropdownMenuSeparator />
                  <DropdownMenuItem asChild>
                    <Link href="/user/logout">로그아웃</Link>
                  </DropdownMenuItem>
                </>
              )}
              {!isLogin && (
                <>
                  <DropdownMenuItem asChild>
                    <Link href="/user/login">로그인</Link>
                  </DropdownMenuItem>
                  <DropdownMenuItem asChild>
                    <Link href="/user/signup">회원가입</Link> 
                  </DropdownMenuItem>
                </>
              )}
            </DropdownMenuContent>
          </DropdownMenu>
        </header>

        <main className="flex flex-col flex-grow justify-center items-center">
          {children}
        </main>

        <footer className="w-full flex justify-center p-4 bg-gray-50">
          <p className="text-muted-foreground text-sm">
            © {new Date().getFullYear()} CoffeeProject. All rights reserved.
          </p>
        </footer>
      </LoginUserContext.Provider>
    </>
  );
}
