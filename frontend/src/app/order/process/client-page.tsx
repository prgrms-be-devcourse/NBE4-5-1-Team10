"use client";

import { Card } from "@/components/ui/card";
import { Input } from "@/components/ui/input";
import { Button } from "@/components/ui/button";
import { RadioGroup, RadioGroupItem } from "@/components/ui/radio-group";
import Image from "next/image";
import { LoginUserContext } from "@/stores/auth/auth-store";
import { use, useState } from "react";
import { components } from "@/lib/backend/generated/schema";
import { CartItemDto, postOrder } from "./page";
import { useRouter, useSearchParams } from "next/navigation";
import { CreditCard } from "lucide-react";
import client from "@/lib/backend/client";

export default function ClientPage() {
  const searchParams = useSearchParams();
  const items = JSON.parse(searchParams.get("items") || "[]") as CartItemDto[];
  const { loginUser } = use(LoginUserContext);
  const [address, setAddress] = useState("");
  const [postalCode, setPostalCode] = useState("");
  const selectedTotal = items.reduce(
    (acc, item) => acc + item.price * item.quantity,
    0
  );
  const shippingFee = selectedTotal > 0 ? 3000 : 0;
  const totalAmount = selectedTotal + shippingFee;
  const tomorrow = new Date();
  tomorrow.setDate(tomorrow.getDate() + 1);

  const year = tomorrow.getFullYear();
  const month = String(tomorrow.getMonth() + 1).padStart(2, "0");
  const day = String(tomorrow.getDate()).padStart(2, "0");

  const tomorrowString = `${year}.${month}.${day}`;
  const isWrite = address.length > 0 && postalCode.length > 0;
  const router = useRouter();

  async function handleCreateOrder() {
    try {
      const res = await fetch("/api/order", {
        method: "POST",
        body: JSON.stringify({
          address,
          postalCode,
          shippingPrice: shippingFee,
          items,
        }),
        credentials: "include",
      });
      if (!res.ok) {
        console.error("Fetch order failed:", res.status);
        return;
      }
    } catch (err) {
      console.error("Fetch cart error:", err);
    }

    router.push("/order/list");
  }

  return (
    <div className="flex justify-center w-full min-h-screen px-8 py-12 bg-gray-100">
      <div className="w-full max-w-screen-lg">
        <h2 className="text-2xl font-bold mb-8">결제 진행</h2>

        <div className="flex gap-8">
          <div className="flex-1 space-y-6">
            {/* 주문자 정보 */}
            <Card className="p-6">
              <h3 className="font-semibold mb-4">주문자 정보</h3>
              <div>이름: {loginUser.username}</div>
            </Card>

            {/* 배송 정보 */}
            <Card className="p-6">
              <h3 className="font-semibold mb-4">배송 정보</h3>
              <Input
                placeholder="주소를 입력해주세요"
                className="mb-4"
                value={address}
                onChange={(e) => setAddress(e.target.value)}
              />
              <Input
                placeholder="우편번호를 입력해주세요"
                className="mb-4"
                value={postalCode}
                onChange={(e) => setPostalCode(e.target.value)}
              />
            </Card>
          </div>

          {/* 결제 요약 */}
          <Card className="w-80 p-6 h-fit">
            <h3 className="font-semibold mb-4">결제 요약</h3>

            <div className="space-y-4">
              {items.map((item) => (
                <div className="flex gap-4" key={item.id}>
                  <div className="w-14 h-14 bg-gray-200 rounded-md flex items-center justify-center">
                    <Image
                      src={item.productImageUrl || ""}
                      alt={item.productName || ""}
                      width={50}
                      height={50}
                      className="rounded-md"
                      style={{ width: 50, height: 50 }}
                    />
                  </div>
                  <div className="text-sm">
                    <p className="font-medium">{item.productName}</p>
                    <p className="font-semibold">
                      {item.price.toLocaleString()}원
                    </p>
                  </div>
                </div>
              ))}
            </div>
            <div className="mt-6 border-t pt-4 space-y-2 text-sm">
              <div className="flex justify-between">
                <span>상품 금액</span>
                <span>{selectedTotal.toLocaleString()}원</span>
              </div>
              <div className="flex justify-between">
                <span>배송비</span>
                <span>{shippingFee.toLocaleString()}원</span>
              </div>

              <div className="flex justify-between font-semibold text-lg border-t pt-4">
                <span>총 결제 금액</span>
                <span>{totalAmount.toLocaleString()}원</span>
              </div>

              <p className="text-xs text-gray-500">
                🚚 예상 도착일: {tomorrowString}
              </p>

              <Button
                className={`w-full mt-4 bg-black text-white ${
                  isWrite
                    ? "bg-black text-white"
                    : "bg-gray-300 text-gray-500 cursor-not-allowed"
                }`}
                disabled={!isWrite}
                onClick={(e) => {
                  e.preventDefault();
                  handleCreateOrder();
                }}
              >
                <CreditCard size={16} />
                결제하기
              </Button>
            </div>
          </Card>
        </div>

        <footer className="mt-12 text-center text-gray-500 text-sm">
          © 2024 Company Name. All rights reserved.
        </footer>
      </div>
    </div>
  );
}
