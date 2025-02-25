"use client";

import { useState } from "react";
import { useRouter } from "next/navigation";
import Image from "next/image";
import Link from "next/link";
import { ShoppingCart } from "lucide-react";

import { Button } from "@/components/ui/button";
import { Card } from "@/components/ui/card";
import Combobox from "@/components/ui/custom/combobox";
import {
  Dialog,
  DialogContent,
  DialogDescription,
  DialogHeader,
  DialogTitle,
} from "@/components/ui/dialog";
import { components } from "@/lib/backend/generated/schema";
import { isValidUrl } from "@/lib/utils";

export default function ClientPage({
  data,
  keyword,
  pageSize,
  page,
}: {
  data: components["schemas"]["RsDataItemsResBody"];
  keyword: string;
  pageSize: number;
  page: number;
}) {
  const router = useRouter();
  const products = data.data?.items || [];

  const [cartModalOpen, setCartModalOpen] = useState(false);

  const handleAddToCart = async (productId: number) => {
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
  };

  return (
    <div className="flex justify-center items-center w-full min-h-screen px-8 py-12">
      <Card className="w-full min-h-screen flex flex-col p-8 gap-6 bg-white shadow-sm shadow-black/10">
        <div className="w-full flex flex-col gap-6">
          <div className="w-full flex justify-center items-center">
            <p className="text-2xl font-semibold text-center">상품 목록</p>
          </div>

          <div className="flex w-full justify-end items-center mt-4 bg-transparent z-1">
            <Combobox
              itemList={[
                { value: "title", label: "상품번호순" },
                { value: "content", label: "인기순" },
              ]}
              title="상품번호 순"
            />
          </div>

          <div className="grid grid-cols-4 gap-6 w-full">
            {products.map((product) => {
              const isValidImage =
                product.imageUrl &&
                product.imageUrl.length > 0 &&
                isValidUrl(product.imageUrl);

              const imageUrl = isValidImage ? product.imageUrl : "/none.jpg";
              return (
                <Card
                  key={product.id}
                  className="p-4 shadow-md border border-gray-200"
                >
                  <Link href={`/product/${product.id}`}>
                    <div className="relative w-full h-40 overflow-hidden rounded-md">
                      <Image
                        src={imageUrl || "/none.jpg"}
                        alt={product.name}
                        fill
                        className="object-cover rounded-md"
                      />
                    </div>
                    <div className="mt-4 space-y-1">
                      <p className="text-lg font-medium">{product.name}</p>
                      <p className="text-xl font-bold">
                        {product.price.toLocaleString()}원
                      </p>
                      <p
                        className={`text-sm ${
                          product.stockQuantity
                            ? "text-green-600"
                            : "text-red-600"
                        }`}
                      >
                        {product.stockQuantity ? "재고 있음" : "재고 없음"}
                      </p>
                    </div>
                  </Link>

                  <Button
                    className={`w-full mt-4 flex items-center gap-2 ${
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
                </Card>
              );
            })}
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
    </div>
  );
}
