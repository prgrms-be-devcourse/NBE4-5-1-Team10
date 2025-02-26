"use client";

import { useEffect, useState } from "react";
import { Card } from "@/components/ui/card";
import { Button } from "@/components/ui/button";
import { CheckCircle } from "lucide-react";
import { components } from "@/lib/backend/generated/schema";
import {
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableHeader,
  TableRow,
} from "@/components/ui/table";
import Loading from "@/components/utils/loading";
import {
  Dialog,
  DialogContent,
  DialogDescription,
  DialogFooter,
  DialogHeader,
  DialogTitle,
} from "@/components/ui/dialog";

const statusIndex = {
  ORDERED: 0, // 주문 완료 → 파란색
  READY_DELIVERY_SAME_DAY: 1, // 당일 배송 준비 → 노란색
  READY_DELIVERY_NEXT_DAY: 1, // 익일 배송 준비 → 주황색
  SHIPPED: 2, // 배송 출발 → 보라색
  DELIVERED: 3, // 배송 완료 → 회색
};

export default function ClientPage({ id }: { id: number }) {
  const [order, setOrder] =
    useState<components["schemas"]["OrderDetailResponse"]>();
  const [isLoading, setIsLoading] = useState(false);
  const [deliveryModalOpen, setDeliveryModalOpen] = useState(false);

  async function initFetchOrder() {
    try {
      const res = await fetch(`/api/admin/order?id=${id}`, {
        method: "GET",
        credentials: "include",
      });
      if (!res.ok) {
        console.error("Fetch order failed:", res.status);
        return;
      }
      const { data } = await res.json();
      setOrder(data);
      setIsLoading(false);
    } catch (err) {
      console.error("Fetch cart error:", err);
    }
  }

  useEffect(() => {
    initFetchOrder();
  }, [deliveryModalOpen]);

  const handleDelivery = async () => {
    const res = await fetch("/api/admin/order/delivery", {
      method: "POST",
      body: JSON.stringify({
        id,
      }),
    });
    if (!res.ok) {
      console.error("Update product failed:", res.status);
      return;
    }
    setDeliveryModalOpen(true);
  };
  if (isLoading || order == undefined) {
    return <Loading />;
  }

  return (
    <div className="flex justify-center items-center w-full min-h-screen px-8 py-12 bg-gray-100">
      <Card className="w-full max-w-screen-lg p-8 bg-white shadow-md">
        <div className="flex justify-between items-center mb-4">
          <h2 className="text-2xl font-semibold text-center mb-8">
            주문 상세 정보
          </h2>
          <Button
            className="bg-black text-white"
            size="lg"
            onClick={() => handleDelivery()}
            disabled={order.orderStatus !== "ORDERED"}
          >
            {order.orderStatus == "ORDERED"
              ? "배송 처리하기"
              : "배송 처리 완료"}
          </Button>
        </div>

        <Table className="mb-8">
          <TableHeader>
            <TableRow className="bg-gray-100">
              <TableHead className="text-gray-700 pl-4">상품명</TableHead>
              <TableHead className="text-gray-700 text-center">수량</TableHead>
              <TableHead className="text-gray-700 text-right pr-4">
                가격
              </TableHead>
            </TableRow>
          </TableHeader>
          <TableBody>
            {order.orderItems.map((item, index) => (
              <TableRow key={index}>
                <TableCell className="pl-4">{item.productName}</TableCell>
                <TableCell className="text-center">
                  {item.quantity} 개
                </TableCell>
                <TableCell className="text-right pr-4">
                  ₩{item.price?.toLocaleString()}
                </TableCell>
              </TableRow>
            ))}
          </TableBody>
        </Table>

        <div className="mt-12">
          <h3 className="text-lg font-semibold mb-6">배송 상태</h3>
          <div className="flex items-center w-full">
            <div className="flex flex-col items-center w-1/4">
              <CheckCircle
                className={`w-8 h-8 ${
                  statusIndex[order.orderStatus] >= 0
                    ? "text-green-500"
                    : "text-gray-300"
                }`}
              />
              <span
                className={`mt-2 text-sm font-medium ${
                  statusIndex[order.orderStatus] >= 0
                    ? "text-green-600"
                    : "text-gray-700"
                }`}
              >
                주문 완료
              </span>
            </div>
            <div
              className={`w-1/4 h-1 ${
                statusIndex[order.orderStatus] >= 1
                  ? "bg-green-500"
                  : "bg-gray-300"
              }`}
            ></div>
            <div className="flex flex-col items-center w-1/4">
              <CheckCircle
                className={`w-8 h-8 ${
                  statusIndex[order.orderStatus] >= 1
                    ? "text-green-500"
                    : "text-gray-300"
                }`}
              />
              <span
                className={`mt-2 text-sm font-medium ${
                  statusIndex[order.orderStatus] >= 1
                    ? "text-green-600"
                    : "text-gray-700"
                }`}
              >
                배송 준비
              </span>
            </div>
            <div
              className={`w-1/4 h-1 ${
                statusIndex[order.orderStatus] >= 2
                  ? "bg-green-500"
                  : "bg-gray-300"
              }`}
            ></div>
            <div className="flex flex-col items-center w-1/4">
              <CheckCircle
                className={`w-8 h-8 ${
                  statusIndex[order.orderStatus] >= 2
                    ? "text-green-500"
                    : "text-gray-300"
                }`}
              />
              <span
                className={`mt-2 text-sm font-medium ${
                  statusIndex[order.orderStatus] >= 2
                    ? "text-green-600"
                    : "text-gray-700"
                }`}
              >
                배송 출발
              </span>
            </div>
            <div
              className={`w-1/4 h-1 ${
                statusIndex[order.orderStatus] >= 3
                  ? "bg-green-500"
                  : "bg-gray-300"
              }`}
            ></div>
            <div className="flex flex-col items-center w-1/4">
              <CheckCircle
                className={`w-8 h-8 ${
                  statusIndex[order.orderStatus] >= 3
                    ? "text-green-500"
                    : "text-gray-300"
                }`}
              />
              <span
                className={`mt-2 text-sm font-medium ${
                  statusIndex[order.orderStatus] >= 3
                    ? "text-green-600"
                    : "text-gray-700"
                }`}
              >
                배송 완료
              </span>
            </div>
          </div>
        </div>
        <div className="grid grid-cols-1 md:grid-cols-2 gap-6 mt-10">
          <div>
            <h3 className="text-lg font-semibold mb-2">배송 정보</h3>
            <p className="text-gray-600">수령인: {order.username}</p>
            <p className="text-gray-600">주소: {order.address}</p>
            <p className="text-gray-600">우편번호: {order.postalCode}</p>
          </div>

          <div>
            <h3 className="text-lg font-semibold mb-2">결제 정보</h3>
            <p className="text-gray-800 font-semibold">
              총 결제금액: ₩{order.totalPrice.toLocaleString()}
            </p>
          </div>
        </div>
      </Card>

      <Dialog open={deliveryModalOpen} onOpenChange={setDeliveryModalOpen}>
        <DialogContent className="sm:max-w-md" aria-describedby="">
          <DialogHeader>
            <DialogTitle>배송 처리 완료</DialogTitle>
            <DialogDescription>
              주문 상품이 성공적으로 발송 처리되었습니다.
            </DialogDescription>
          </DialogHeader>

          <DialogFooter>
            <Button
              onClick={(e) => {
                e.preventDefault();
                setDeliveryModalOpen(false);
              }}
            >
              확인
            </Button>
          </DialogFooter>
        </DialogContent>
      </Dialog>
    </div>
  );
}
