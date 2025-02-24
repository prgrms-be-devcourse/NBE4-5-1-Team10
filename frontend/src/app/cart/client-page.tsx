"use client";

import { Button } from "@/components/ui/button";
import { Card } from "@/components/ui/card";
import { components } from "@/lib/backend/generated/schema";
import Image from "next/image";
import { useCallback, useEffect, useMemo, useState } from "react";
import debounce from "lodash/debounce";
import {
  TableHeader,
  TableRow,
  TableHead,
  TableBody,
  TableCell,
  Table,
} from "@/components/ui/table";
import { Checkbox } from "@/components/ui/checkbox";
import { Input } from "@/components/ui/input";

type CartDto = {
  id: number;
  userId: number;
  cartItems: components["schemas"]["CartItemDto"][];
};

type CartItemDto = components["schemas"]["CartItemDto"] & {
  selected: boolean;
};

export default function ClientPage() {
  const [cart, setCart] = useState<CartDto | null>(null);
  const [cartItems, setCartItems] = useState<CartItemDto[]>([]);

  async function fetchCart() {
    try {
      const res = await fetch("/api/cart", {
        method: "GET",
        credentials: "include",
      });
      if (!res.ok) {
        console.error("Fetch cart failed:", res.status);
        return;
      }
      const data = await res.json();
      setCart(data.data as CartDto);
    } catch (err) {
      console.error("Fetch cart error:", err);
    }
  }

  // 화면 초기화
  useEffect(() => {
    fetchCart();
  }, []);

  //cart 변경 감지 시, cartItems에 복사 (selected 기본값 true)
  useEffect(() => {
    if (cart?.cartItems) {
      setCartItems(
        cart.cartItems.map((item) => ({
          ...item,
          selected: true,
        }))
      );
    }
  }, [cart]);

  // productId와 quantity만 넘기면 500ms 후 updateCartAPI 호출
  const debouncedUpdateCart = useMemo(
    () =>
      debounce(async (productId: number, quantity: number) => {
        if (quantity < 0) return;
        try {
          const res = await fetch("/api/cart", {
            method: "PATCH",
            credentials: "include",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({ productId, quantity }),
          });
          if (!res.ok) {
            console.error("Patch cart failed:", res.status);
            return;
          }
          const updatedCart = await res.json();
          setCart(updatedCart.data as CartDto);
        } catch (error) {
          console.error("장바구니 업데이트 중 오류가 발생했습니다.", error);
        }
      }, 500),
    []
  );

  // 수량 변경 시 상태 업데이트 + 디바운스 API 호출
  const updateProductQuantity = useCallback(
    (productId: number, quantity: number) => {
      setCartItems((prev) =>
        prev.map((item) =>
          item.productId === productId
            ? { ...item, quantity: Math.max(quantity, 0) }
            : item
        )
      );
      debouncedUpdateCart(productId, Math.max(quantity, 0));
    },
    [debouncedUpdateCart]
  );

  const toggleSelect = useCallback((cartItemId: number) => {
    setCartItems((prev) =>
      prev.map((item) =>
        item.id === cartItemId ? { ...item, selected: !item.selected } : item
      )
    );
  }, []);

  if (!cart) {
    return <div>장바구니 데이터를 불러오는 중입니다...</div>;
  }

  const selectedItems = cartItems.filter((item) => item.selected);
  const selectedTotal = selectedItems.reduce(
    (acc, item) => acc + item.price * item.quantity,
    0
  );
  const shippingFee = selectedTotal > 0 ? 3000 : 0;
  const totalAmount = selectedTotal + shippingFee;

  return (
    <div className="flex justify-center items-center w-full min-h-screen px-8 py-12">
      <Card className="w-full max-w-screen-lg p-8 bg-white shadow-md">
        <h2 className="text-2xl font-semibold text-center mb-6">장바구니</h2>

        <Table className="w-full">
          <TableHeader>
            <TableRow>
              <TableHead className="text-center">선택</TableHead>
              <TableHead className="text-left">제품</TableHead>
              <TableHead className="text-center">수량</TableHead>
              <TableHead className="text-center whitespace-nowrap">
                가격
              </TableHead>
              <TableHead className="text-right whitespace-nowrap">
                합계
              </TableHead>
            </TableRow>
          </TableHeader>
          <TableBody>
            {cartItems.map((item) => (
              <TableRow key={item.id} className="h-20">
                <TableCell className="text-center">
                  <Checkbox
                    checked={item.selected}
                    onCheckedChange={() => toggleSelect(item.id)}
                  />
                </TableCell>

                <TableCell className="text-left">
                  <div className="flex items-center gap-4">
                    <Image
                      src={item.productImageUrl || ""}
                      alt={item.productName}
                      width={50}
                      height={50}
                      className="rounded-md"
                    />
                    <p>{item.productName}</p>
                  </div>
                </TableCell>

                <TableCell className="text-center">
                  <div className="flex justify-center items-center gap-2">
                    <Button
                      variant="outline"
                      size="sm"
                      disabled={item.quantity <= 0}
                      onClick={() =>
                        updateProductQuantity(item.productId, item.quantity - 1)
                      }
                    >
                      -
                    </Button>
                    <Input
                      type="number"
                      value={item.quantity}
                      onChange={(e) =>
                        updateProductQuantity(
                          item.productId,
                          Number(e.target.value)
                        )
                      }
                      className="w-16 text-center"
                    />
                    <Button
                      variant="outline"
                      size="sm"
                      onClick={() =>
                        updateProductQuantity(item.productId, item.quantity + 1)
                      }
                    >
                      +
                    </Button>
                  </div>
                </TableCell>

                <TableCell className="text-center whitespace-nowrap text-gray-700">
                  {item.price.toLocaleString()}원
                </TableCell>

                <TableCell className="text-right whitespace-nowrap font-semibold">
                  {(item.price * item.quantity).toLocaleString()}원
                </TableCell>
              </TableRow>
            ))}
          </TableBody>
        </Table>

        {/* 결제 요약 정보 */}
        <Card className="mt-8 p-4 bg-gray-100 rounded-md">
          <div className="flex justify-between py-2">
            <span>선택상품 금액</span>
            <span className="font-semibold">
              {selectedTotal.toLocaleString()}원
            </span>
          </div>
          <div className="flex justify-between py-2">
            <span>배송비</span>
            <span className="font-semibold">
              {shippingFee.toLocaleString()}원
            </span>
          </div>
          <div className="flex justify-between py-2 text-lg font-bold">
            <span>총 주문금액</span>
            <span>{totalAmount.toLocaleString()}원</span>
          </div>
        </Card>

        <div className="mt-6 flex justify-end">
          <Button className="px-6 py-3 text-lg">선택상품 주문하기</Button>
        </div>
      </Card>
    </div>
  );
}
