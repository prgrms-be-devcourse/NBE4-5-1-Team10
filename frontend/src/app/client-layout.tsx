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
import { usePathname, useRouter } from "next/navigation";
import AuthProvider from "@/providers/auth.provider";

export default function ClientLayout({
  children,
}: Readonly<{
  children: React.ReactNode;
}>) {
  const pathname = usePathname();

  const router = useRouter();

  const {
    setLoginUser,
    isLogin,
    loginUser,
    removeLoginUser,
    isLoginUserPending,
    isAdmin,
    setAnonymousUser,
  } = useLoginUser();

  const loginUserContextValue = {
    loginUser,
    setLoginUser,
    removeLoginUser,
    isLogin,
    isLoginUserPending,
    isAdmin,
    setAnonymousUser,
  };

  async function fetchUser() {
    try {
      const res = await fetch("/api/user", {
        method: "GET",
      });
      if (!res.ok) {
        return;
      }
      const data = await res.json();
      return data.data!!;
    } catch (err) {
      return;
    }
  }

  async function logout() {
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
  }

  useEffect(() => {
    fetchUser()
      .then((userResponse) => {
        if (!userResponse) {
          removeLoginUser();
        } else {
          const user = userResponse!!;
          setLoginUser(user);
        }
      })
      .catch(() => removeLoginUser());
  }, []);

  return (
    <>
      <LoginUserContext.Provider value={loginUserContextValue}>
        <header className="w-full bg-white shadow-sm py-4 px-8 flex items-center justify-center">
          {isAdmin ? (
            <Link href="/admin" className="absolute left-8 text-xl font-bold">
              CoffeeProject
            </Link>
          ) : (
            <Link href="/" className="absolute left-8 text-xl font-bold">
              CoffeeProject
            </Link>
          )}
          <NavigationMenu>
            <NavigationMenuList className="gap-4">
              {isAdmin ? (
                <>
                  <NavigationMenuItem>
                    <Link href="/admin" legacyBehavior passHref>
                      <NavigationMenuLink
                        className={`relative px-4 py-2 text-sm font-medium rounded-md transition-all ${
                          pathname === "/admin"
                            ? "text-[#6F4E37]"
                            : "text-sm hover:text-[#D2B48C]"
                        }`}
                      >
                        대시보드
                        {pathname === "/admin" && (
                          <span className="absolute left-0 bottom-0 w-full h-1 bg-[#D2B48C] rounded-full"></span>
                        )}
                      </NavigationMenuLink>
                    </Link>
                  </NavigationMenuItem>
                  <NavigationMenuItem>
                    <Link href="/admin/product/list" legacyBehavior passHref>
                      <NavigationMenuLink
                        className={`relative px-4 py-2 text-sm font-medium rounded-md transition-all ${
                          pathname === "/admin/product/list"
                            ? "text-[#6F4E37]"
                            : "text-sm hover:text-[#D2B48C]"
                        }`}
                      >
                        상품 관리
                        {pathname === "/admin/product/list" && (
                          <span className="absolute left-0 bottom-0 w-full h-1 bg-[#D2B48C] rounded-full"></span>
                        )}
                      </NavigationMenuLink>
                    </Link>
                  </NavigationMenuItem>
                  <NavigationMenuItem>
                    <Link href="/admin/order/list" legacyBehavior passHref>
                      <NavigationMenuLink
                        className={`relative px-4 py-2 text-sm font-medium rounded-md transition-all ${
                          pathname === "/admin/order/list"
                            ? "text-[#6F4E37]"
                            : "text-sm hover:text-[#D2B48C]"
                        }`}
                      >
                        주문 관리
                        {pathname === "/admin/order/list" && (
                          <span className="absolute left-0 bottom-0 w-full h-1 bg-[#D2B48C] rounded-full"></span>
                        )}
                      </NavigationMenuLink>
                    </Link>
                  </NavigationMenuItem>
                </>
              ) : (
                <>
                  <NavigationMenuItem>
                    <Link href="/" legacyBehavior passHref>
                      <NavigationMenuLink
                        className={`relative px-4 py-2 text-sm font-medium rounded-md transition-all ${
                          pathname === "/"
                            ? "text-[#6F4E37]"
                            : "text-sm hover:text-[#D2B48C]"
                        }`}
                      >
                        Home
                        {pathname === "/" && (
                          <span className="absolute left-0 bottom-0 w-full h-1 bg-[#D2B48C] rounded-full"></span>
                        )}
                      </NavigationMenuLink>
                    </Link>
                  </NavigationMenuItem>
                </>
              )}
            </NavigationMenuList>
          </NavigationMenu>

          {isLogin ? (
            <DropdownMenu>
              <DropdownMenuTrigger asChild>
                <Button variant="outline" className="absolute right-8 gap-2">
                  <FontAwesomeIcon icon={faHouse} />
                  {loginUser.username}님
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
                    {!isAdmin && (
                      <>
                        <DropdownMenuItem asChild>
                          <Link href="/cart">장바구니</Link>
                        </DropdownMenuItem>
                        <DropdownMenuItem asChild>
                          <Link href="/order/list">주문 내역</Link>
                        </DropdownMenuItem>
                      </>
                    )}

                    <DropdownMenuSeparator />
                    <DropdownMenuItem onClick={logout}>
                      로그아웃
                    </DropdownMenuItem>
                  </>
                )}
              </DropdownMenuContent>
            </DropdownMenu>
          ) : (
            <Button
              variant="outline"
              className="absolute right-8 gap-2"
              onClick={() => router.push("/user/login")}
            >
              <FontAwesomeIcon icon={faHouse} />
              로그인
            </Button>
          )}
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
