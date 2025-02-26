"use client";

import { Button } from "@/components/ui/button";
import { Card } from "@/components/ui/card";
import Combobox from "@/components/ui/custom/combobox";
import { components } from "@/lib/backend/generated/schema";
import { ShoppingCart, Star } from "lucide-react";
import { useRouter } from "next/navigation";
import Image from "next/image";
import { use, useState } from "react";
import {
  Dialog,
  DialogContent,
  DialogDescription,
  DialogHeader,
  DialogTitle,
} from "@/components/ui/dialog";
import { LoginUserContext } from "@/stores/auth/auth-store";

export default function ClientPage({
  product,
  reviews,
}: {
  product: components["schemas"]["ProductGetItemDto"];
  reviews: any;
}) {
  const router = useRouter();
  const { isLogin } = use(LoginUserContext);
  const [data, setData] = useState(product);
  const [cartModalOpen, setCartModalOpen] = useState(false);
  const [loginModalOpen, setLoginModalOpen] = useState(false);

  const handleAddToCart = async (productId: number) => {
    if (isLogin) {
      try {
        const res = await fetch("/api/cart", {
          method: "POST",
          credentials: "include",
          headers: { "Content-Type": "application/json" },
          body: JSON.stringify({ productId, quantity: 1 }),
        });
        if (!res.ok) {
          console.error("Post cart failed:", res.status);
          return;
        }
        setCartModalOpen(true);
      } catch (error) {
        console.error(error);
      }
    } else {
      setLoginModalOpen(true);
    }
  };
  return (
    <div className="flex justify-center items-center w-full min-h-screen px-8 py-12 bg-gray-100">
      <Card className="w-full max-w-screen-lg p-8 bg-white shadow-md">
        <div className="flex flex-col md:flex-row gap-10">
          <div className="w-full md:w-1/2">
            <Image
              src={data.imageUrl || ""}
              alt={data.name || ""}
              width={500}
              height={400}
              className="w-full h-auto rounded-md object-cover"
              style={{ width: 500, height: 400 }}
            />
          </div>

          <div className="w-full md:w-1/2 flex flex-col justify-center">
            <h2 className="text-2xl font-semibold">{data.name}</h2>
            <p className="text-3xl font-bold mt-2">
              {Number(data.price).toLocaleString()}원
            </p>
            <p className="text-gray-600 mt-4">{data.description}</p>

            <Button
              className={`w-full mt-6 flex items-center gap-2 ${
                product.stockQuantity
                  ? "bg-black text-white"
                  : "bg-gray-300 text-gray-500 cursor-not-allowed"
              }`}
              disabled={!product.stockQuantity}
              onClick={(e) => {
                e.preventDefault();
                handleAddToCart(product.id);
              }}
            >
              <ShoppingCart size={16} />
              장바구니 담기
            </Button>
          </div>
        </div>

        <div className="mt-12">
          <h3 className="text-xl font-semibold mb-4">상품 리뷰</h3>
          <div className="space-y-4">
            {reviews.map((review: any) => (
              <Card
                key={review.id}
                className="p-4 flex flex-col md:flex-row items-start justify-between bg-gray-50 border"
              >
                <div>
                  <div className="flex items-center gap-1 text-yellow-500">
                    {Array.from({ length: review.rating }).map((_, i) => (
                      <Star key={i} size={16} fill="currentColor" />
                    ))}
                  </div>
                  <p className="font-medium">{review.user}</p>
                  <p className="text-gray-700 mt-1">{review.comment}</p>
                </div>
                <p className="text-gray-500 text-sm">{review.date}</p>
              </Card>
            ))}
          </div>
        </div>
      </Card>

      <Dialog open={cartModalOpen} onOpenChange={setCartModalOpen}>
        <DialogContent className="sm:max-w-md" aria-describedby="">
          <DialogHeader>
            <DialogTitle>장바구니에 담겼습니다!</DialogTitle>
            <DialogDescription />
          </DialogHeader>
          <div className="flex justify-between mt-4">
            <Button variant="outline" onClick={() => setCartModalOpen(false)}>
              쇼핑 계속하기
            </Button>
            <Button
              className="bg-black text-white"
              onClick={() => router.push("/cart")}
            >
              장바구니로 이동
            </Button>
          </div>
        </DialogContent>
      </Dialog>

      <Dialog open={loginModalOpen} onOpenChange={setLoginModalOpen}>
        <DialogContent className="sm:max-w-md">
          <DialogHeader>
            <DialogTitle>로그인이 필요합니다</DialogTitle>
            <DialogDescription>
              이 기능을 이용하려면 먼저 로그인해야 합니다.
              <br />
              로그인 페이지로 이동하시겠습니까?
            </DialogDescription>
          </DialogHeader>
          <div className="flex justify-end mt-4 space-x-2">
            <Button variant="outline" onClick={() => setLoginModalOpen(false)}>
              취소
            </Button>
            <Button
              className="bg-black text-white"
              onClick={() => {
                setLoginModalOpen(false);
                router.push("/user/login");
              }}
            >
              로그인 하러 가기
            </Button>
          </div>
        </DialogContent>
      </Dialog>
    </div>
  );
}
