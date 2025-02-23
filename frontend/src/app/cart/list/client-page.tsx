"use client";

import { Button } from "@/components/ui/button";
import { Card } from "@/components/ui/card";
import { components } from "@/lib/backend/generated/schema";
import Image from "next/image";
import { useCallback, useEffect, useState } from "react";
import { debounce } from "lodash";
import { getCartAPI, updateCartAPI } from "./page";
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

export default function ClientPage({
  data,
}: {
  data: components["schemas"]["CartDto"];
}) {
  const [cart, setCart] = useState(data);
  const [cartItems, setCartItems] = useState(
    data.cartItems?.map((item) => ({
      selected: true,
      ...item,
    })) || []
  );

  const debouncedUpdateCart = useCallback(
    debounce((productId: number, quantity: number) => {
      if (quantity < 0) return;
      updateCartAPI(productId, quantity);
    }, 500),
    []
  );

  useEffect(() => {
    getCartAPI().then((data) => {
      if (data) setCart(data);
    });
  }, [cart]);

  const updateProductQuantity = (productId: number, quantity: number) => {
    setCartItems((prev) =>
      prev?.map((item) =>
        item.productId == productId
          ? { ...item, quantity: Math.max(quantity, 0) }
          : item
      )
    );
    debouncedUpdateCart(productId, quantity);
  };

  const toggleSelect = (cartItemId: number) => {
    setCartItems((prev) =>
      prev.map((item) =>
        item.id === cartItemId ? { ...item, selected: !item.selected } : item
      )
    );
  };

  const selectedItems = cartItems.filter((item) => item.selected);
  const selectedTotal = selectedItems.reduce(
    (acc, item) => acc + item.totalPrice,
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
                      disabled={!(item.quantity > 0)}
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
